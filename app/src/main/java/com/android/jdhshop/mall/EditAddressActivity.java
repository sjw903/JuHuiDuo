package com.android.jdhshop.mall;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.mallbean.AddressBean;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class EditAddressActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_name)
    EditText txtName;
    @BindView(R.id.txt_phone)
    EditText txtPhone;
    @BindView(R.id.txt_address_two)
    EditText txtAddressTwo;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.txt_address_one)
    TextView txtAddressOne;
    @BindView(R.id.cb_default)
    CheckBox cbDefault;
    private AddressBean addressBean;
    private CityPickerView cityDialog;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("添加/编辑地址");
    }

    @Override
    protected void initData() {
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
                txtAddressOne.setText(province.getName() + " " + city.getName() + " " +  district.getName());
                if (addressBean != null) {
                    addressBean.province = province.getName();
                    addressBean.city = city.getName();
                    addressBean.county = district.getName();
                }
            }

            @Override
            public void onCancel() {
            }
        });
        if (getIntent().getBundleExtra("address") != null) {
            addressBean = (AddressBean) getIntent().getBundleExtra("address").get("address");
            txtName.setText(addressBean.consignee);
            txtPhone.setText(addressBean.contact_number);
            txtAddressOne.setText(addressBean.province + " " + addressBean.city + " " + addressBean.county);
            txtAddressTwo.setText(addressBean.detail_address);
            cbDefault.setChecked("Y".equals(addressBean.is_default));
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListener() {
        cbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (addressBean != null) {
                    addressBean.is_default = isChecked?"Y":"N";
                }
            }
        });
    }

    @OnClick({R.id.tv_left, R.id.txt_address_one, R.id.btn_save, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.txt_address_one:
                cityDialog.showCityPicker();
                break;
            case R.id.btn_save:
                if (addressBean != null) {
                    editAddress();
                } else {
                    if (TextUtils.isEmpty(txtName.getText().toString())) {
                        showToast("请填写姓名");
                        return;
                    }
                    if (TextUtils.isEmpty(txtPhone.getText().toString())) {
                        showToast("请填写联系方式");
                        return;
                    }
                    if (TextUtils.isEmpty(txtAddressOne.getText().toString())) {
                        showToast("请选择地址");
                        return;
                    }
                    if (TextUtils.isEmpty(txtAddressTwo.getText().toString())) {
                        showToast("请填写详细地址");
                        return;
                    }
                    addAddress();
                }
                break;
            case R.id.btn_delete:
                showTipDialog3("删除提示", "确定删除该收货地址吗", new onClickListener() {
                    @Override
                    public void onClickSure() {
                        deleteAddress();
                    }
                }, new onClickListener() {
                    @Override
                    public void onClickSure() {

                    }
                },"确定","取消");
                break;
        }
    }

    private void addAddress() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("province", txtAddressOne.getText().toString().split(" ")[0]);
        requestParams.put("city", txtAddressOne.getText().toString().split(" ")[1]);
        requestParams.put("county", txtAddressOne.getText().toString().split(" ")[2]);
        requestParams.put("detail_address", txtAddressTwo.getText().toString());
        requestParams.put("consignee", txtName.getText().toString());
        requestParams.put("contact_number", txtPhone.getText().toString());
        requestParams.put("is_default", cbDefault.isChecked()?"Y":"N");
        HttpUtils.post(Constants.ADD_ADDRESS, EditAddressActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    JSONObject temp = new JSONObject(responseString);
                    if (temp.getInt("code") == 0) {
                        showToast("添加成功");
                        finish();
                    } else {
                        showToast(temp.getString("msg"));
                        if ("用户不存在".equals(temp.getString("msg"))) {
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void editAddress() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("address_id", addressBean.id);
        requestParams.put("province", addressBean.province);
        requestParams.put("city", addressBean.city);
        requestParams.put("county", addressBean.county);
        requestParams.put("detail_address", txtAddressTwo.getText().toString().trim());
        requestParams.put("consignee", txtName.getText().toString().trim());
        requestParams.put("contact_number", txtPhone.getText().toString().trim());
        requestParams.put("is_default",cbDefault.isChecked()?"Y":"N");
        HttpUtils.post(Constants.EDIT_ADDRESS, EditAddressActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    JSONObject temp = new JSONObject(responseString);
                    if (temp.getInt("code") == 0) {
                        showToast("编辑成功");
                        finish();
                    } else {
                        showToast(temp.getString("msg"));
                        if ("用户不存在".equals(temp.getString("msg"))) {
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteAddress() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("address_id", addressBean.id);
        HttpUtils.post(Constants.DELETE_ADDRESS, EditAddressActivity.this,requestParams, new TextHttpResponseHandler() {
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
                    JSONObject temp = new JSONObject(responseString);
                    if (temp.getInt("code") == 0) {
                        showToast("删除成功");
                        finish();
                    } else {
                        showToast(temp.getString("msg"));
                        if ("用户不存在".equals(temp.getString("msg"))) {
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
