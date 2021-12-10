package com.android.jdhshop.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.NormalAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.ShareBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.utils.ImgUtils;
import com.android.jdhshop.utils.WxUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class NewShareActivity extends BaseActivity implements IUiListener {

    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.txt_shouyi)
    TextView txtShouyi;
    @BindView(R.id.txt_goods_name)
    TextView txtGoodsName;
    @BindView(R.id.txt_on_price)
    TextView txtOnPrice;
    @BindView(R.id.txt_after_price)
    TextView txtAfterPrice;
    @BindView(R.id.txt_last_shouyi)
    TextView txtLastShouyi;
    @BindView(R.id.txt_last_tkp)
    TextView txtLastTkp;
    @BindView(R.id.checks)
    CheckBox checks;
    @BindView(R.id.et_wenan)
    EditText editText;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.img_grid)
    RecyclerView imgGrid;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.txt_edit)
    TextView txtEdit;
    @BindView(R.id.img_check)
    CheckBox imgCheck;
    @BindView(R.id.view_zz)
    View viewZz;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    private List<String> urls;
    private List<ShareBean> shareBeans = new ArrayList<>();
    private NormalAdapter adapter;
    private Bitmap mainBit = null;
    String storePath = "";
    List<String> tempList=new ArrayList<>();
    @Override
    protected void initUI() {
        setContentView(R.layout.ac_newshare);
        ButterKnife.bind(this);
        tvTitle.setText("创建分享");
        tvLeft.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        imgGrid.setLayoutManager(gridLayoutManager);
        adapter = new NormalAdapter(this, shareBeans);
        imgGrid.setAdapter(adapter);
        txtShouyi.setText("奖励收益预估:¥" + getIntent().getStringExtra("shouyi"));
        txtGoodsName.setText(getIntent().getStringExtra("name"));
          txtOnPrice.setText("【在售价】" + getIntent().getStringExtra("price") + "元");
        txtAfterPrice.setText("【券后价】" + getIntent().getStringExtra("after_price") + "元");
        txtLastShouyi.setText("【下载"+ getString(R.string.app_name) +"再省】" + getIntent().getStringExtra("shouyi") + "元");
        txtLastTkp.setText("複->製这条信息" + getIntent().getStringExtra("kouling").replaceFirst("¥","(").replaceFirst("¥",")"));
        urls = getIntent().getBundleExtra("bitmap").getStringArrayList("urls");
        storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hkxsave/";
        Glide.with(this).load(new File(storePath + getIntent().getStringExtra("imgurl"))).asBitmap().skipMemoryCache(true).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                img.setImageBitmap(bitmap);
                mainBit = bitmap;
            }
        });
        for (int i = 0; i < urls.size() && i < 6; i++) {
            shareBeans.add(new ShareBean("N", urls.get(i)));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String one=SPUtils.getStringData(this,"mbone","");
        String two=SPUtils.getStringData(this,"mbtwo","");
        String three=SPUtils.getStringData(this,"mbthree","");
        String four=SPUtils.getStringData(this,"mbfour","");
        String five=SPUtils.getStringData(this,"mbfive","");
        editText.setText(Html.fromHtml(one.replace("{标题}",getIntent().getStringExtra("name"))+"<br/>"+two.replace("{商品原价}",getIntent().getStringExtra("price"))+"<br/>"+
                three.replace("{券后价}",getIntent().getStringExtra("after_price"))+"<br/>"+
                four.replace("{佣金}",getIntent().getStringExtra("shouyi"))+ "<br/>"+
                "----------------<br/><br/>"+five.replace("{淘口令}",getIntent().getStringExtra("kouling").replaceFirst("¥","(").replaceFirst("¥",")"))));

    }

    @Override
    protected void initListener() {
        checks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtLastShouyi.setVisibility(View.VISIBLE);
                } else {
                    txtLastShouyi.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.txt_edit,R.id.tv_left, R.id.btn_copy, R.id.btn_invite, R.id.copy_taobao_btn, R.id.copy_friends_cicle_btn, R.id.copy_friends_qq, R.id.copy_friends_cicle_zone, R.id.copy_friends_btn, R.id.view_zz})
    public void onViewClicked(View view) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label",editText.getText().toString());

        switch (view.getId()) {
            case R.id.txt_edit:
                openActivity(ChangeShareMuBanActivity.class);
//                if(editText.isFocusable()){
//                    editText.setFocusable(false);
//                    txtEdit.setText("编辑分享文案");
//                    editText.setFocusableInTouchMode(false);
//                }else{
//                    txtEdit.setText("完成编辑 ");
//                    editText.setFocusableInTouchMode(true);
//                    editText.setFocusable(true);
//                    editText.requestFocus();
//                    editText.setSelection(editText.getText().toString().length());
//                }
                break;
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_copy:
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                T.showShort(this, "复制成功");
                JAnalyticsInterface.onEvent(NewShareActivity.this,new CountEvent("tb_copy_tkl"));
                break;
            case R.id.copy_taobao_btn://
                saveImgs(getBitmap());
                T.showShort(this, "保存成功");
                viewZz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                break;
            case R.id.copy_friends_cicle_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(this, "请安装微信客户端");
                    return;
                }
                List<Bitmap> bitmaps=getBitmap();
                if(bitmaps.size()>1){
                    T.showShort(this, "朋友圈仅支持单图分享");
                    return;
                }
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                share(1, bitmaps);
                break;
            case R.id.copy_friends_btn:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mm") == null) {
                    T.showShort(this, "请安装微信客户端");
                    return;
                }
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                share(0, getBitmap());
                break;
            case R.id.copy_friends_qq:
                if (getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq") == null) {
                    T.showShort(this, "请安装QQ客户端");
                    return;
                }
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                share(2, getBitmap());
                break;
            case R.id.copy_friends_cicle_zone:
                if (getPackageManager().getLaunchIntentForPackage("com.qzone") == null) {
                    T.showShort(this, "请安装QQ空间客户端");
                    return;
                }
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                share(3, getBitmap());
                break;
            case R.id.view_zz:
                viewZz.setVisibility(View.GONE);
                llShare.setVisibility(View.GONE);
                break;
            case R.id.btn_invite:
                viewZz.setVisibility(View.VISIBLE);
                llShare.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if("Y".equals(SPUtils.getStringData(this,"share_zd","N"))){
            if(tempList.size()>0){
                for(int i=0;i<tempList.size();i++){
                    if(new File(storePath+tempList.get(i)).exists()){
//                        new File(storePath+tempList.get(i)).delete();
                        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        ContentResolver mContentResolver = getContentResolver();
                        String where = MediaStore.Images.Media.DATA + "='" + storePath+tempList.get(i) + "'";
                        mContentResolver.delete(uri, where, null);
                    }
                }
            }
            tempList.clear();
            SPUtils.saveStringData(getApplicationContext(),"share_zd","N");
        }
    }

    private void share(final int type, final List<Bitmap> list) {
        if(list.size()<=0){
            T.showShort(this, "未选择分享图片");
            return;
        }
        if(type==1){
            for(int i=0;i<list.size();i++){
                tempList.add(urls.get(i));
            }
            SPUtils.saveStringData(getApplicationContext(),"share_zd","Y");
            WxUtil.sharePicByBitMap(list.get(0), "pyq", SendMessageToWX.Req.WXSceneTimeline, this);
            return;
        }
        final List<String> urls = saveImgs(list);
        tempList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
               final Intent intent = new Intent();
                ComponentName comp = null;
                if (type == 2 || type == 3) {
                    if (type == 2) {
                        comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
                    } else {
                        comp = new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");
                    }
                } else if (type == 0 || type == 1) {
                    if (type == 0) {
                        comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                    } else {
                        comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                    }
                }
                intent.setComponent(comp);
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("image/*");
               final ArrayList<Uri> imageUris = new ArrayList<>();
                for (int i = 0; i < urls.size(); i++) {
                    if(type==0||type==1){
                        imageUris.add(getImageContentUri(getApplicationContext(),new File(storePath + urls.get(i))));
                    }else{
                        imageUris.add(Uri.parse(storePath + urls.get(i)));
                    }
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                intent.putExtra("Kdescription", "");
                for(int i=0;i<list.size();i++){
                    tempList.add(urls.get(i));
                }
                SPUtils.saveStringData(getApplicationContext(),"share_zd","Y");
                startActivity(intent);
                JAnalyticsInterface.onEvent(NewShareActivity.this,new CountEvent("tb_share_goods"));
            }
        }).start();
        viewZz.setVisibility(View.GONE);
        llShare.setVisibility(View.GONE);
    }
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        Uri uri = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }
            cursor.close();
        }

        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        return uri;
    }

    private List<String> saveImgs(List<Bitmap> list) {
        if(tempList.size()>0){
            for(int i=0;i<tempList.size();i++){
                if(new File(storePath+tempList.get(i)).exists()){
//                        new File(storePath+tempList.get(i)).delete();
                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver mContentResolver = getContentResolver();
                    String where = MediaStore.Images.Media.DATA + "='" + storePath+tempList.get(i) + "'";
                    mContentResolver.delete(uri, where, null);
                }
            }
        }
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String url = System.currentTimeMillis() + i + ".jpg";
            if (ImgUtils.saveImageToGallery2(this, list.get(i),url)) {
                urls.add(url);
            }
        }
        return urls;
    }

    private List<Bitmap> getBitmap() {
        List<Bitmap> list = new ArrayList<>();
        if (imgCheck.isChecked() && mainBit != null) {
            list.add(mainBit);
        }
        List<CheckBox> checkBoxes = adapter.getCheckBoxes();
        List<Bitmap> bitmaps = adapter.getBitmaps();
        for (int i = 0; i < bitmaps.size(); i++) {
            if (checkBoxes.get(i).isChecked() && bitmaps.get(i) != null) {
                list.add(bitmaps.get(i));
            }
        }
        return list;
    }

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
