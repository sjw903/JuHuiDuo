package com.android.jdhshop.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.adapter.ImageAdapter;
import com.android.jdhshop.adapter.ImageFolderAdapter;
import com.android.jdhshop.bean.Image;
import com.android.jdhshop.bean.ImageFolder;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.StatusBarUtil;
import com.android.jdhshop.utils.TDevice;
import com.android.jdhshop.widget.ImageFolderView;
import com.android.jdhshop.widget.recyclerview.MultiTypeSupport;
import com.android.jdhshop.widget.recyclerview.SpaceGridItemDecoration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectImageActivity extends BaseImgSelActivity implements ImageFolderView.ImageFolderViewListener, ImageAdapter.onCameraClickListener {
    // ???????????????????????????EXTRA_KEY
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final int MAX_SIZE = 9;
    private static final int PERMISSION_REQUEST_CODE = 88;
    private static final int TAKE_PHOTO = 99;
    @BindView(R.id.tv_back)
    TextView mTvBack;
    @BindView(R.id.tv_ok)
    TextView mTvSelectCount;
    @BindView(R.id.rv)
    RecyclerView mRvImage;
    @BindView(R.id.tv_photo)
    TextView mTvPhoto;
    @BindView(R.id.tv_preview)
    TextView mTvPreview;
    @BindView(R.id.image_folder_view)
    ImageFolderView mImageFolderView;
    private boolean mHasCamera = true;
    //????????????????????????
    private List<Image> mSelectedImages = new ArrayList<>();
    private List<Image> mImages = new ArrayList<>();
    private List<ImageFolder> mImageFolders = new ArrayList<>();
    private ImageAdapter mImageAdapter;
    private ImageFolderAdapter mImageFolderAdapter;
    private Uri mImageUri;
    private File takePhotoImageFile;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_image;
    }

    @Override
    protected void init() {
        //????????????????????????
        StatusBarUtil.statusBarTintColor(this, ContextCompat.getColor(this, R.color.black));
        setupSelectedImages();
        mRvImage.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        mRvImage.addItemDecoration(new SpaceGridItemDecoration((int) TDevice.dipToPx(getResources(), 1)));
        //??????????????????
        getSupportLoaderManager().initLoader(0, null, mLoaderCallbacks);
        mImageFolderView.setListener(this);
        Bitmap bitmap17 = getLoacalBitmap(Constants.ZIYUAN_PATH+"icon_back_while.png");
        BitmapDrawable bd=new BitmapDrawable(bitmap17);
        if (android.os.Build.VERSION.RELEASE.equals("10") || android.os.Build.VERSION.RELEASE.equals("11") ){
            bd.setBounds(0, 0, 50, 50);// ???????????????setBounds();
            mTvBack.setCompoundDrawables(null,bd,null,null);
        }else{
            bd.setBounds(0, 0, bd.getMinimumWidth(), bd.getMinimumHeight());// ???????????????setBounds();
            mTvBack.setCompoundDrawables(null,bd,null,null);
        }

    }
    /**
     * ??????????????????
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///???????????????Bitmap??????

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void setupSelectedImages() {
        Intent intent = getIntent();
        ArrayList<Image> selectImages = intent.getParcelableArrayListExtra("selected_images");
        mSelectedImages.addAll(selectImages);

        if (mSelectedImages.size() > 0 && mSelectedImages.size() <= MAX_SIZE) {
            mTvPreview.setClickable(true);
            mTvPreview.setText(String.format("??????(%d/9) ", mSelectedImages.size()));
            mTvPreview.setTextColor(ContextCompat.getColor(SelectImageActivity.this, R.color.colorAccent));
        }
    }


    @OnClick({R.id.tv_back, R.id.tv_ok, R.id.tv_photo, R.id.tv_preview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_ok:
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(EXTRA_RESULT, (ArrayList<? extends Parcelable>) mSelectedImages);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.tv_photo:
                if (mImageFolderView.isShowing()) {
                    mImageFolderView.hide();
                } else {
                    mImageFolderView.show();
                }
                break;
            case R.id.tv_preview:
                Intent previewIntent = new Intent(this, PreviewImageActivity.class);
                previewIntent.putParcelableArrayListExtra("preview_images", (ArrayList<? extends Parcelable>) mSelectedImages);
                startActivity(previewIntent);
                break;
        }
    }


    private void addImageFoldersToAdapter() {
        if (mImageFolderAdapter == null) {
            mImageFolderAdapter = new ImageFolderAdapter(this, mImageFolders, R.layout.item_list_folder);
            mImageFolderView.setAdapter(mImageFolderAdapter);
        } else {
            mImageFolderAdapter.notifyDataSetChanged();
        }
    }

    private void addImagesToAdapter(ArrayList<Image> images) {
        mImages.clear();
        mImages.addAll(images);
        if (mImageAdapter == null) {
            mImageAdapter = new ImageAdapter(this, mImages, mSelectedImages, mMultiTypeSupport);
            mRvImage.setAdapter(mImageAdapter);
        } else {
            mImageAdapter.notifyDataSetChanged();
        }
        mImageAdapter.setSelectImageCountListener(mOnSelectImageCountListener);
        mImageAdapter.setOnCameraClickListener(this);
    }

    private MultiTypeSupport<Image> mMultiTypeSupport = image -> {
        if (TextUtils.isEmpty(image.getPath())) {
            return R.layout.item_list_camera;
        }
        return R.layout.item_list_image;
    };
    /*************************************?????????????????????????????????************************************************/

    private ImageAdapter.onSelectImageCountListener mOnSelectImageCountListener = new ImageAdapter.onSelectImageCountListener() {
        @Override
        public void onSelectImageCount(int count) {
            if (count == 0) {
                mTvPreview.setClickable(false);
                mTvPreview.setText("??????");
                mTvPreview.setTextColor(ContextCompat.getColor(SelectImageActivity.this, R.color.gray));
            } else if (count > 0 && count <= MAX_SIZE) {
                mTvPreview.setClickable(true);
                mTvPreview.setText(String.format("??????(%d/9) ", count));
                mTvPreview.setTextColor(ContextCompat.getColor(SelectImageActivity.this, R.color.colorAccent));
            }
        }

        @Override
        public void onSelectImageList(List<Image> images) {
            mSelectedImages = images;
        }
    };

    /*************************************????????????????????????************************************************/

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        //????????????CursorLoader?????????????????????????????????
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return new CursorLoader(SelectImageActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null, null, IMAGE_PROJECTION[2] + " DESC");
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                ArrayList<Image> images = new ArrayList<>();
                //????????????????????????
                if (mHasCamera) {
                    //???????????????????????????????????????
                    images.add(new Image());
                }
                ImageFolder defaultFolder = new ImageFolder();
                defaultFolder.setName("????????????");
                defaultFolder.setPath("");
                mImageFolders.add(defaultFolder);

                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        String thumbPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        String bucket = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));

                        Image image = new Image();
                        image.setPath(path);
                        image.setName(name);
                        image.setDate(dateTime);
                        image.setId(id);
                        image.setThumbPath(thumbPath);
                        image.setFolderName(bucket);
                        images.add(image);
                        //???????????????????????????
                        if (mSelectedImages.size() > 0) {
                            for (Image i : mSelectedImages) {
                                if (i.getPath().equals(image.getPath())) {
                                    image.setSelect(true);
                                }
                            }
                        }
                        //??????????????????????????????
                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        ImageFolder folder = new ImageFolder();
                        folder.setName(folderFile.getName());
                        folder.setPath(folderFile.getAbsolutePath());
                        //ImageFolder?????????equal?????????equal????????????????????????????????????
                        if (!mImageFolders.contains(folder)) {
                            folder.getImages().add(image);
                            //??????????????????
                            folder.setAlbumPath(image.getPath());
                            mImageFolders.add(folder);
                        } else {
                            ImageFolder imageFolder = mImageFolders.get(mImageFolders.indexOf(folder));
                            imageFolder.getImages().add(image);
                        }
                    } while (data.moveToNext());
                }
                addImagesToAdapter(images);
                //????????????
                defaultFolder.getImages().addAll(images);
                if (mHasCamera) {
                    defaultFolder.setAlbumPath(images.size() > 1 ? images.get(1).getPath() : null);
                } else {
                    defaultFolder.setAlbumPath(images.size() > 0 ? images.get(0).getPath() : null);
                }
                //????????????????????????????????????????????????????????????????????????
                if (mSelectedImages.size() > 0) {
                    List<Image> rs = new ArrayList<>();
                    for (Image i : mSelectedImages) {
                        File f = new File(i.getPath());
                        if (!f.exists()) {
                            rs.add(i);
                        }
                    }
                    mSelectedImages.removeAll(rs);
                }
            }
            mImageFolderView.setImageFolders(mImageFolders);
            addImageFoldersToAdapter();
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };


    @Override
    public void onSelectFolder(ImageFolderView imageFolderView, ImageFolder imageFolder) {
        addImagesToAdapter(imageFolder.getImages());
        mRvImage.scrollToPosition(0);
        mTvPhoto.setText(imageFolder.getName());
    }

    @Override
    public void onDismiss() {

    }

    @Override
    public void onShow() {

    }

    /*************************************????????????************************************************/

    @Override
    public void onCameraClick() {
        //???????????????????????????
        int isPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (isPermission == PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        } else {
            //????????????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                Toast.makeText(this, "????????????????????????!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void takePhoto() {
        //?????????????????????Intent
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            takePhotoImageFile = createImageFile();
            if (takePhotoImageFile != null) {
                Log.i("take photo", takePhotoImageFile.getAbsolutePath());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ///7.0???????????????FileProvider???File?????????Uri
                    mImageUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", takePhotoImageFile);
                } else {
                    //7.0?????????????????????Uri???fromFile?????????File?????????Uri
                    mImageUri = Uri.fromFile(takePhotoImageFile);
                }
                //????????????????????????Uri???????????????
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                //????????????
                startActivityForResult(takePhotoIntent, TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO) {
            //????????????????????????????????????intent??????Bundle???????????????Bundle????????????data????????????Intent????????? Bundle?????????data?????????Bitmap??????
            // Bundle extras = data.getExtras();
            // Bitmap bitmap = (Bitmap) extras.get("data");
//            BitmapFactory.decodeFile(this.getContentResolver().)
//            galleryAddPictures(mImageUri);
//            getSupportLoaderManager().restartLoader(0, null, mLoaderCallbacks);
            galleryAddPictures();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(mImageUri));
                Log.i("take photo", bitmap + "");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    /**
     * ??????????????????????????????
     */
    private void galleryAddPictures() {
        //??????????????????????????????
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    takePhotoImageFile.getAbsolutePath(), takePhotoImageFile.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //??????????????????
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(takePhotoImageFile));
        sendBroadcast(mediaScanIntent);
    }
}
