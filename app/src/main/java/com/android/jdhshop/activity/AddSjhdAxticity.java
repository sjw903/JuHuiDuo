package com.android.jdhshop.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.Image;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.bean.SjhdBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.utils.TDevice;
import com.android.jdhshop.widget.recyclerview.SpaceGridItemDecoration;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class AddSjhdAxticity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.name_edit)
    EditText name_edit;
    @BindView(R.id.phone_edit)
    EditText phone_edit;
    @BindView(R.id.nums_edit)
    EditText nums_edit;
    @BindView(R.id.bt_button)
    Button bt_button;
    @BindView(R.id.img_image)
    ImageView img_image;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.gridView1)
    GridView gridView1;//?????????????????????
    @BindView(R.id.rv_selected_image)
    RecyclerView mSelectedImageRv;

    private File avaterFile = null;
    private String pathImage;                //??????????????????
    private Bitmap bmp;                      //??????????????????
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //?????????
    private final int IMAGE_OPEN = 1;        //??????????????????

    private static final int REQUEST_IMAGE3 = 5;
    private ArrayList<String> strings = new ArrayList<>();
    private static final String[] authBaseArr = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ArrayList<Image> mSelectImages = new ArrayList<>();
    private static final int SELECT_IMAGE_REQUEST = 0x0011;
    File file;


    @Override
    protected void initUI() {
        setContentView(R.layout.sjhd_yhb_bmxx);
        ButterKnife.bind(this);
        BaseLogDZiYuan.LogDingZiYuan(imageView1, "icon_events_pic_1.png");
        /*

         * ???????????????????????????
         * ?????????????????????activity?????? android:windowSoftInputMode="adjustPan"
         * ???????????????????????? android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        //????????????
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        mSelectedImageRv.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        mSelectedImageRv.addItemDecoration(new SpaceGridItemDecoration((int) TDevice.dipToPx(getResources(), 1)));
    }


    @Override
    protected void initData() {
        tvTitle.setText("????????????");
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
         * ????????????????????????????????????
         * ?????????????????????
         * SimpleAdapter??????imageItem???????????? R.layout.griditem_addpic?????????
         */
        //????????????????????????
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_events_pic_1);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[]{"itemImage"}, new int[]{R.id.imageView1});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);
        /*
         * ??????GridView????????????
         * ??????:??????????????????????????? ?????????????????????import android.view.View;
         */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) { //?????????????????????+ 0??????0?????????
                    Intent intent = new Intent(AddSjhdAxticity.this, SelectImageActivity.class);
                    intent.putParcelableArrayListExtra("selected_images", mSelectImages);
                    startActivityForResult(intent, SELECT_IMAGE_REQUEST);
                    //??????onResume()????????????
                } else {
                    dialog(position);
                }
            }
        });
    }

    @Override
    protected void initListener() {
        bt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(name_edit.getText().toString())) {
                    showToast("???????????????");
                    return;
                }
                if ("".equals(phone_edit.getText().toString())) {
                    showToast("???????????????");
                    return;
                }
                if ("".equals(nums_edit.getText().toString())) {
                    showToast("?????????????????????");
                    return;
                }
                if (!isMobileNO(phone_edit.getText().toString())) {
                    showToast("????????????????????????");
                    return;
                }
                imageItem = new ArrayList<HashMap<String, Object>>();
                JSONArray array = new JSONArray();
                for (int i = 0; i < mSelectImages.size(); i++) {
                    array.put(mSelectImages.get(i).getPath());
                }
                add(array);
            }
        });
    }

    /**
     * ??????????????????
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * ?????????134???135???136???137???138???139???150???151???157(TD)???158???159???187???188
         * ?????????130???131???132???152???155???156???185???186 ?????????133???153???180???189??????1349?????????
         * ????????????????????????????????????1?????????????????????3???5???8???????????????????????????0-9
         */
        String telRegex = "[1][3456789]\\d{9}";// "[1]"?????????1????????????1???"[358]"????????????????????????3???5???8???????????????"\\d{9}"????????????????????????0???9???????????????9??????
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    @Override
    protected void ReceiverBroadCastMessage(String status, String result, Serializable serializable, Intent intent) {
        super.ReceiverBroadCastMessage(status, result, serializable, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * @??????:????????????
     * @?????????:??????
     * @??????:2018/7/26 17:05
     */
    private void add(JSONArray array) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("activity_id", getIntent().getStringExtra("activityId"));
        requestParams.put("name", name_edit.getText().toString());
        requestParams.put("phone", phone_edit.getText().toString());
        requestParams.put("num", nums_edit.getText().toString());
        requestParams.put("works", "" + array);

        HttpUtils.postUpload(Constants.ADDHD, AddSjhdAxticity.this, requestParams, new onOKJsonHttpResponseHandler<SjhdBean>(new TypeToken<Response<SjhdBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Response<SjhdBean> datas) {
                if (datas.isSuccess()) {
                    showToast("????????????");
                    finish();
                } else {
                    showToast(datas.getMsg());
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_REQUEST && data != null) {
                ArrayList<Image> selectImages = data.getParcelableArrayListExtra(SelectImageActivity.EXTRA_RESULT);
                mSelectImages.clear();
                mSelectImages.addAll(selectImages);

                bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_events_pic_1);
                imageItem = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> map = null;
                map = new HashMap<String, Object>();
                map.put("itemImage", bmp);
                imageItem.add(map);
                for (int i = 0; i < mSelectImages.size(); i++) {
                    map = new HashMap<String, Object>();
                    map.put("itemImage", mSelectImages.get(i).getPath());
                    imageItem.add(map);
                }

//                Uri selectedImage = data.getParcelableArrayListExtra(SelectImageActivity.EXTRA_RESULT);
//                //??????URL???file??????
//                file = uri2File(selectedImage);
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String picPath = cursor.getString(columnIndex);
//                //??????
//                String encode = null;
//                try {
//                    encode = URLEncoder.encode(picPath, "utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                cursor.close();
//                if (picPath.equals("")) return;
//                Log.e("123", "onActivityResult: ??????:--" + encode);

                simpleAdapter = new SimpleAdapter(this,
                        imageItem, R.layout.griditem_addpic,
                        new String[]{"itemImage"}, new int[]{R.id.imageView1});
                simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data,
                                                String textRepresentation) {
                        // TODO Auto-generated method stub
                        if (view instanceof ImageView && data instanceof Bitmap) {
                            ImageView i = (ImageView) view;
                            i.setImageBitmap((Bitmap) data);
                            return true;
                        }
                        return false;
                    }
                });
                gridView1.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
//                mAdapter = new SelectedImageAdapter(this, mSelectImages, R.layout.selected_image_item,new String[] { "itemImage"}, new int[] { R.id.imageView1});
//                imgAdapter = new ImageListAdapter(this, mSelectImages);
//                gridView1.setAdapter(imgAdapter);

            }
        }
    }

    /**
     * user?????????file??????
     * ????????????file??????
     *
     * @param uri
     * @return
     */
    private File uri2File(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null,
                null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor
                    .getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }


    //????????????
    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
     * Dialog?????????????????????????????????
     * position?????????????????????
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSjhdAxticity.this);
        builder.setMessage("?????????????????????????????????");
        builder.setTitle("??????");
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
