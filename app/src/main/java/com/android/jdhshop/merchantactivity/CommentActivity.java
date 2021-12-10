package com.android.jdhshop.merchantactivity;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.widget.ImageSelectDialog;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
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

public class CommentActivity extends BaseActivity {

    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cb_one)
    CheckBox cbOne;
    @BindView(R.id.cb_two)
    CheckBox cbTwo;
    @BindView(R.id.cb_three)
    CheckBox cbThree;
    @BindView(R.id.cb_four)
    CheckBox cbFour;
    @BindView(R.id.cb_five)
    CheckBox cbFive;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.grid_view1)
    RecyclerView gridView;
    List<String> imgs = new ArrayList<>();
    MyAdapter adapter;
    int score=1;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("订单评论");
        tvLeft.setVisibility(View.VISIBLE);
        imgs.add("");
        adapter = new MyAdapter(R.layout.img_select, imgs);
        gridView.setLayoutManager(new GridLayoutManager(this, 3));
        gridView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.img_del) {
                    imgs.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    if(position==(imgs.size()-1))
                    new ImageSelectDialog(getComeActivity(), R.style.ActionSheetDialogStyle).setOnImageSelectDialogListener(new ImageSelectDialog.onImageSelectDialogListener() {
                        @Override
                        public void onImageSelectResult(String picturePath) {
                            if (picturePath == null || "".equals(picturePath))
                                return;
                            imgs.add(0, picturePath);
                            adapter.notifyDataSetChanged();
                        }
                    }).show();
                }

            }
        });
    }
    /**
     * @属性:上传头像
     * @开发者:陈飞
     * @时间:2018/7/23 20:23
     */
    private void editUserAvatarRequest() {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.put("score", score);
            requestParams.put("order_id",getIntent().getStringExtra("order_id"));
            requestParams.put("goods_id",getIntent().getStringExtra("goods_id"));
            requestParams.put("content",etContent.getText().toString());
            if(imgs.size()>1){
                for(int i=0;i<imgs.size()-1;i++){
                    requestParams.put("img["+i+"]",new File(imgs.get(i)));
                }
            }
            HttpUtils.postUpload(Constants.comment, CommentActivity.this,requestParams, new TextHttpResponseHandler() {
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
                        int code = jsonObject.optInt("code");
                        String msg = jsonObject.optString("msg");
                        if (code == 0) {
                            showToast(msg);
                            finish();
                        } else {
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
    @Override
    protected void initListener() {
        cbOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cbOne.isChecked()) {
                            score=1;
                        } else {
                            cbTwo.setChecked(false);
                            cbThree.setChecked(false);
                            cbFour.setChecked(false);
                            cbFive.setChecked(false);
                        }
                    }
                }, 100);
            }
        });
        cbTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cbTwo.isChecked()) {
                            cbOne.setChecked(true);
                            score=2;
                        } else {
                            cbThree.setChecked(false);
                            cbFour.setChecked(false);
                            cbFive.setChecked(false);
                        }
                    }
                }, 100);
            }
        });
        cbThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cbThree.isChecked()) {
                            cbOne.setChecked(true);
                            cbTwo.setChecked(true);
                            score=3;
                        } else {
                            cbFour.setChecked(false);
                            cbFive.setChecked(false);
                        }
                    }
                }, 100);
            }
        });
        cbFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cbFour.isChecked()) {
                            cbOne.setChecked(true);
                            cbThree.setChecked(true);
                            cbTwo.setChecked(true);
                            score=4;
                        } else {
                            cbFive.setChecked(false);
                        }
                    }
                }, 100);
            }
        });
        cbFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cbFive.isChecked()) {
                            cbOne.setChecked(true);
                            cbThree.setChecked(true);
                            cbTwo.setChecked(true);
                            cbFour.setChecked(true);
                            score=5;
                        } else {
                        }
                    }
                }, 100);
            }
        });
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    ToastUtils.showShortToast(CommentActivity.this, "请输入评论");
                    return;
                }
                editUserAvatarRequest();
            }
        });
    }

    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        finish();
    }

    class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public MyAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ImageView imageView = helper.getView(R.id.img);
            if (helper.getAdapterPosition() == (mData.size() - 1)) {
                helper.getView(R.id.img_del).setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.add);
            } else {
                helper.getView(R.id.img_del).setVisibility(View.VISIBLE);
                Glide.with(mContext).load(item).into(imageView);
            }
            helper.addOnClickListener(R.id.img_del).addOnClickListener(R.id.img);

        }
    }

}
