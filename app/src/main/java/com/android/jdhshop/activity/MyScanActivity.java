package com.android.jdhshop.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.king.zxing.CaptureHelper;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页  扫一扫功能
 */
public class MyScanActivity extends BaseActivity implements OnCaptureCallback {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.sw_light)
    Switch aSwitch;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.viewfinderView)
    ViewfinderView viewfinderView;
    @BindView(R.id.from_dcim)
    LinearLayout from_dcim;
    private String TAG = getClass().getSimpleName();
    private BaseActivity baseActivity;
    private boolean isContinuousScan;
    private CaptureHelper mCaptureHelper;
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_my_scan);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        Intent trans = getIntent();
        if (trans!=null && trans.getStringExtra("set_title")!=null){
            tvTitle.setText(trans.getStringExtra("set_title"));
        }
        else {
            tvTitle.setText("扫码领券");
        }
        tvLeft.setVisibility(View.VISIBLE);
        isContinuousScan = getIntent().getBooleanExtra(KEY_IS_CONTINUOUS,false);

        from_dcim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseActivity = (BaseActivity) getComeActivity();
                baseActivity.selectPhoto(getComeActivity(),1000);
            }
        });

        mCaptureHelper = new CaptureHelper(this,surfaceView,viewfinderView);
        mCaptureHelper.setOnCaptureCallback(this);
        mCaptureHelper.onCreate();
        mCaptureHelper.vibrate(true)
                .fullScreenScan(true)//全屏扫码
                .supportVerticalCode(true)//支持扫垂直条码，建议有此需求时才使用。
                .continuousScan(isContinuousScan);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    openFlash();
                }else{
                    offFlash();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(aSwitch.isChecked()){
//            offFlash();
//        }
        mCaptureHelper.onDestroy();
    }
    private void offFlash(){
        Camera camera = mCaptureHelper.getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }

    public void openFlash(){
        Camera camera = mCaptureHelper.getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCaptureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    @Override
    protected void initListener() {

    }
    @OnClick(R.id.tv_left)
    public void onViewClicked() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onResultCallback(String result) {
        // Log.d(TAG, "onResultCallback: " + result);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Log.d(TAG, "onActivityResult: " + requestCode + " , " + resultCode + "," + data);
        String picturePath;
        if (resultCode == RESULT_OK&&requestCode==1000&&data!=null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                int[] bit = new int[bitmap.getWidth()*bitmap.getHeight()];
                bitmap.getPixels(bit,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
                RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(),bitmap.getHeight(),bit);
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                com.google.zxing.Reader reader = new MultiFormatReader();
                Result result = null;
                result = reader.decode(binaryBitmap);
                String result_text = "";
                if (result!=null) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", result.getText());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    // Log.d(TAG, "从相册解析二维码结果: " + result.getText());
                    result_text = result.getText();
                }
                else
                {
                    showTipDialog("解析二维码失败！");
                }

                Intent result_intent = new Intent();
                result_intent.putExtra("isfromdcim",1);
                result_intent.putExtra("text",result_text);
                setResult(1000,result_intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
