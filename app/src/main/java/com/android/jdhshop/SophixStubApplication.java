package com.android.jdhshop;


public class SophixStubApplication extends CaiNiaoApplication {
//    private final String TAG = "SophixStubApplication";
//    private Context context;
//    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
//    @Keep
//    @SophixEntry(CaiNiaoApplication.class)
//    static class RealApplicationStub {
//    }
//
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        this.context=base;
////         如果需要使用MultiDex，需要在此处调用。
////         MultiDex.install(this);
//        initSophix();
//    }
//
//    private void initSophix() {
//        String appVersion = "1.4.4.28";
//        try {
//            appVersion = this.getPackageManager()
//                    .getPackageInfo(this.getPackageName(), 0)
//                    .versionName;
//        } catch (Exception e) {
//        }
//        final SophixManager instance = SophixManager.getInstance();
//        instance.setContext(this)
//                .setAppVersion(appVersion)
//                .setSecretMetaData(null, null, null)
//                .setEnableDebug(true)
//                .setEnableFullLog()
//                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
//                    @Override
//                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
//                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
////                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
////                            SophixManager.getInstance().killProcessSafely();
//                        }
//                    }
//                }).initialize();
//    }
}
