package com.android.jdhshop.fragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.common.T;
import com.android.jdhshop.zip.ZhuanHuan;
import com.android.jdhshop.ziyuan.ResourceManager;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.BaoYouActivity;
import com.android.jdhshop.activity.DouActivity;
import com.android.jdhshop.activity.ElemeActivity;
import com.android.jdhshop.activity.GaoyongActivity;
import com.android.jdhshop.activity.GuoyedanActivity;
import com.android.jdhshop.activity.JdActivity;
import com.android.jdhshop.activity.JiayoulistActivity;
import com.android.jdhshop.activity.PHBActivity;
import com.android.jdhshop.activity.PddActivity;
import com.android.jdhshop.activity.WebViewActivity;
import com.android.jdhshop.activity.WebViewActivity3;
import com.android.jdhshop.activity.WphActivity;
import com.android.jdhshop.activity.ZeroBuyActivity;
import com.android.jdhshop.base.BaseLazyFragment;
import com.android.jdhshop.bean.PddClient;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.merchantactivity.MerchanthomeActivity;
import com.android.jdhshop.suning.SuningHomeActivity;
import com.android.jdhshop.utils.SignUtils;
import com.tencent.mm.opensdk.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ChaoJiDaoHangFragment extends BaseLazyFragment {
    @BindView(R.id.pdd_ziyuan)
    ImageView pdd_ziyuan;
    @BindView(R.id.jd_ziyuan)
    ImageView jd_ziyuan;
    @BindView(R.id.tianmao_ziyuan)
    ImageView tianmao_ziyuan;
    @BindView(R.id.tianmaochaoshi_ziyuan)
    ImageView tianmaochaoshi_ziyuan;
    @BindView(R.id.meizhuang_ziyuan)
    ImageView meizhuang_ziyuan;
    @BindView(R.id.guoji_ziyuan)
    ImageView guoji_ziyuan;
    @BindView(R.id.tb_ziyuan)
    ImageView tb_ziyuan;
    @BindView(R.id.tbqiangg_ziyuan)
    ImageView tbqiangg_ziyuan;
    @BindView(R.id.chaojiquan_ziyuan)
    ImageView chaojiquan_ziyuan;
    @BindView(R.id.lingyuan_ziyuan)
    ImageView lingyuan_ziyuan;
    @BindView(R.id.phb_ziyuan)
    ImageView phb_ziyuan;
    @BindView(R.id.jingx_ziyuan)
    ImageView jingx_ziyuan;
    @BindView(R.id.guoye_ziyuan)
    ImageView guoye_ziyuan;
    @BindView(R.id.jianlou_ziyuan)
    ImageView jianlou_ziyuan;
    @BindView(R.id.shipin_ziyuan)
    ImageView shipin_ziyuan;
    @BindView(R.id.juhuas_ziyuan)
    ImageView juhuas_ziyuan;
    @BindView(R.id.douquan_ziyuan)
    ImageView douquan_ziyuan;
    @BindView(R.id.chaoji_title)
    ImageView chaoji_title;
    @BindView(R.id.chaoji_image)
    ImageView chaoji_image;

    @Override
    protected void lazyload() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cjdh, container, false);
        ButterKnife.bind(this, view);

        //加载本地图片
        BaseLogDZiYuan.LogDingZiYuan(pdd_ziyuan,"icon_super_a.png");
        BaseLogDZiYuan.LogDingZiYuan(chaoji_image,"title_supnav_taobao.png");
        BaseLogDZiYuan.LogDingZiYuan(chaoji_title,"bg_supnav_top.png");
        BaseLogDZiYuan.LogDingZiYuan(jd_ziyuan,"icon_super_b.png");
        BaseLogDZiYuan.LogDingZiYuan(tianmao_ziyuan,"icon_taobao_a.png");
        BaseLogDZiYuan.LogDingZiYuan(tianmaochaoshi_ziyuan,"icon_taobao_b.png");
        BaseLogDZiYuan.LogDingZiYuan(meizhuang_ziyuan,"icon_taobao_c.png");
        BaseLogDZiYuan.LogDingZiYuan(guoji_ziyuan,"icon_taobao_d.png");
        BaseLogDZiYuan.LogDingZiYuan(tb_ziyuan,"icon_taobao_e.png");
        BaseLogDZiYuan.LogDingZiYuan(tbqiangg_ziyuan,"icon_taobao_f.png");
        BaseLogDZiYuan.LogDingZiYuan(chaojiquan_ziyuan,"icon_taobao_g.png");
        BaseLogDZiYuan.LogDingZiYuan(lingyuan_ziyuan,"icon_taobao_h.png");
        BaseLogDZiYuan.LogDingZiYuan(phb_ziyuan,"icon_taobao_i.png");
        BaseLogDZiYuan.LogDingZiYuan(jingx_ziyuan,"icon_taobao_j.png");
        BaseLogDZiYuan.LogDingZiYuan(guoye_ziyuan,"icon_taobao_k.png");
        BaseLogDZiYuan.LogDingZiYuan(jianlou_ziyuan,"icon_taobao_l.png");
        BaseLogDZiYuan.LogDingZiYuan(shipin_ziyuan,"icon_life_h.png");
        BaseLogDZiYuan.LogDingZiYuan(juhuas_ziyuan,"icon_taobao_o.png");
        BaseLogDZiYuan.LogDingZiYuan(douquan_ziyuan,"icon_taobao_p.png");
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getInfo(String type) {

        if ("".equals(SPUtils.getStringData(getContext(), "token", ""))) {
            //T.showShortBottom(this.getContext(),"用户不存在");
            openActivity(WelActivity.class);
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("type", type);
        HttpUtils.post(Constants.APP_IP + "/api/MeiTuan/getQudaoLinkUrl",ChaoJiDaoHangFragment.this, requestParams, new TextHttpResponseHandler() {
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
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", "1".equals(type) ? "美团外卖" : "到店支付");
                        intent.putExtra("url", object.getString("data"));
                        startActivity(intent);
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public static String getSign4(Map<String, String> map) {
        map = PddClient.sortMapByKey2(map);
        String temp = "";
        for (String key : map.keySet()) {
            temp += (key + "=" + map.get(key) + "&");
        }
        return getSign(temp.substring(0, temp.length() - 1));
    }

    private void getToken() {
        RequestParams requestParams = new RequestParams();
        long time = new Date().getTime();
        requestParams.put("platformCode", "920062401");
        requestParams.put("timestamp", time + "");
        requestParams.put("phone", SPUtils.getStringData(context, "phone", ""));
        requestParams.put("seq", time + "");
        Map<String, String> map = new HashMap<>();
        map.put("platformCode", "920062401");
        map.put("timestamp", time + "");
        map.put("seq", time + "");
        map.put("phone", SPUtils.getStringData(context, "phone", ""));
        requestParams.put("sig", getSign4(map));
        HttpUtils.post("https://tch.fleetingpower.com/api/v1/queryUserToken/",ChaoJiDaoHangFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject o = new JSONObject(responseString);
                    if (0 == o.getInt("ret")) {
                        String token = o.getJSONObject("data").getString("token");
                        getPosition(token);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        });
    }

    // 声明定位回调监听器
    Double longitudestr = 0.0;
    String tokens = "";
    Double latitudestr = 0.0;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            closeLoadingDialog();
            if (amapLocation == null) {
//                Log.i(TAG, "amapLocation is null!");
                return;
            }
            if (amapLocation.getErrorCode() != 0) {
                ToastUtils.showLongToast(context, "定位失败，请检查设备是否开启定位服务");
                return;
            }
            Double longitude = amapLocation.getLongitude();//获取经度
            Double latitude = amapLocation.getLatitude();//获取纬度
            longitudestr = longitude;
            latitudestr = latitude;
            getUrl();
//            if (Content.wds == 0.0&& Content.jds == 0.0){
//
//            }else {
//
//            }
//            Log.i(TAG, "longitude:" + longitude + ",latitude：" + latitude+"\n"+"记录的："+Content.jds+","+Content.wds);
        }
    };

    private void getUrl() {
        RequestParams requestParams = new RequestParams();
        long time = new Date().getTime();
        requestParams.put("platformCode", "920062401");
        requestParams.put("timestamp", time + "");
        requestParams.put("userLatStr", latitudestr + "");
        requestParams.put("userLngStr", longitudestr + "");
        requestParams.put("seq", time + "");
        requestParams.put("token", tokens);
        Map<String, String> map = new HashMap<>();
        map.put("platformCode", "920062401");
        map.put("timestamp", time + "");
        map.put("seq", time + "");
        map.put("token", tokens);
        map.put("userLatStr", latitudestr + "");
        map.put("userLngStr", longitudestr + "");
        requestParams.put("sig", getSign4(map));
        HttpUtils.post1("https://tch.fleetingpower.com/api/v1/getStationListUrl/", ChaoJiDaoHangFragment.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject o = new JSONObject(responseString);
                    if (0 == o.getInt("ret")) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", "充电站");
                        intent.putExtra("url", o.getJSONObject("data").getString("url"));
                        intent.putExtra("ua", o.getJSONObject("data").getString("seq"));
                        startActivity(intent);
//                        String token=o.getJSONObject("data").getString("token");
//                        getPosition(token);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        });
    }

    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    // 声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public void getPosition(String token) {
        tokens = token;
        showLoadingDialog("定位中");
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60000 * 5);
        // 获取一次定位结果： //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }

    /* 密钥内容 base64 code */
    private static String PUCLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdSA1k2MJd6ePph761YUgLrh48\n" +
                    "wIUWfvPmcy14ZE8v0cZhtwk02gNyE2rBQ/3HAnCVg8+CWyXneitIoe8e6ldiuzNx\n" +
                    "Y44hq90/nEOsmkzWdyPsrQybmd1Vb/HF/17F4nec3gXw6ZICWMLH/qUY9nPTGQZi\n" +
                    "v5vXoPtBVCgXrZ3yvwIDAQAB\n";
    private static String PRIVATE_KEY =
            "MIICXQIBAAKBgQDdSA1k2MJd6ePph761YUgLrh48wIUWfvPmcy14ZE8v0cZhtwk0\n" +
                    "2gNyE2rBQ/3HAnCVg8+CWyXneitIoe8e6ldiuzNxY44hq90/nEOsmkzWdyPsrQyb\n" +
                    "md1Vb/HF/17F4nec3gXw6ZICWMLH/qUY9nPTGQZiv5vXoPtBVCgXrZ3yvwIDAQAB\n" +
                    "AoGAL+Xq0EuDNyTrqp8xjr1yBOU5sljR9h6g2N9Rll/QLD+yO3CNU51lZYoCb7cx\n" +
                    "9aP3jsWrY0IroEF3oQ88XWrIYzWQS7kSz47hyeu7bqfIshMs+kajsxf9yJn7P6lp\n" +
                    "sDJLrmHiqrSXx74wLpnKcWqAk4uTHeYh966Di4Q461pBRTECQQD5PDRDrhsQcNdl\n" +
                    "b/Y2HcX2mivJ0FC+LtywtWnwg0ZDyW7FHoleFqXoYN0s2i7bH4x3dJ+8EpE/nJjZ\n" +
                    "N8GF899NAkEA40mdCDmqr8FH4mtvl9fVNJTqb7Sp7glXJlEnrT2RO6V4POecQJ5T\n" +
                    "/npNGSni+sZA2ovspC8MaHUtztg8HIlsOwJBAM+pPu7JSRmIu590CxQJ+KDA0g1D\n" +
                    "+ZKMnyrI7O0No+TlF9s71z7C5hdZZc9yNyox/iqlzFW6rrTuuFf8Yc1HZd0CQQCy\n" +
                    "7p8LfKqdVuJKpB3kQmx8yseNTYOB/CR56+X6gr+1X107RXNDg+HIM2xC5TDmD/G6\n" +
                    "m/Geh9OS4L1BXAZmyOFFAkBUhuvotV77f32CAdXj4b7BCP4+gEHz6Nldegi9tc9T\n" +
                    "h5uWGkqtjKlWU48Cv2/gt2Ownjm86PU3n4N6b8dyRWE3";

    private static String getSign(String source) {
        String sign = "";
        try {
            RSAPrivateKey publicKey = SignUtils.loadPrivateKeyByStr(PRIVATE_KEY);
            sign = SignUtils.sign(source, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    //R.id.icon_life_dy,
    @OnClick({R.id.icon_life_cd, R.id.icon_life_cha, R.id.icon_life_shegnhuo, R.id.icon_life_kendeji, R.id.icon_life_maidanglao, R.id.icon_life_meituan, R.id.icon_life_meituan1, R.id.icon_life_sp, R.id.icon_super_a, R.id.icon_super_b, R.id.icon_super_c, R.id.icon_super_d, R.id.icon_taobao_a, R.id.icon_taobao_b, R.id.icon_taobao_c, R.id.icon_taobao_d, R.id.icon_taobao_e, R.id.icon_taobao_f, R.id.icon_taobao_g, R.id.icon_taobao_h, R.id.icon_taobao_i, R.id.icon_taobao_j, R.id.icon_taobao_k, R.id.icon_taobao_l, R.id.icon_taobao_m, R.id.icon_life_h, R.id.icon_taobao_n, R.id.icon_taobao_o, R.id.icon_taobao_p, R.id.icon_life_a, R.id.icon_life_b, R.id.icon_life_c, R.id.icon_life_d, R.id.icon_life_e, R.id.icon_life_f, R.id.icon_life_g})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.icon_life_dy:
//                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                req.userName = "gh_5afd2977e019";
//                req.path = "pages/movie/index?scene=oM9RbtwwIAgjsuEbvCLvFGOSVGMQ";
//                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
//                CaiNiaoApplication.api.sendReq(req);
//                break;
            case R.id.icon_life_shegnhuo:
                try {
                    // 以下固定写法
                    final Intent intent1e = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("alipays://platformapi/startapp?appId=20000193"));
                    intent1e.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent1e);
                } catch (Exception e) {
                    // 防止没有安装的情况
                    e.printStackTrace();
                    ToastUtils.showLongToast(context, "请先安装支付宝");
                }
                break;
            case R.id.icon_life_cha:
                toCha();
                break;
            case R.id.icon_life_kendeji:
                RequestParams params = new RequestParams();
                params.put("share_code", "127SIwqxj");
                long timeStamp2 = System.currentTimeMillis();
                params.put("user_id", SPUtils.getStringData(context, "uid", "1"));
                params.put("timestamp", timeStamp2);
//        params.put("secretKey","C78266CF1A8848B48ECF96C0BD5EE465");
                Map<String, String> map = new HashMap<>();
                map.put("shareCode", "127SIwqxj");
                map.put("userId", SPUtils.getStringData(context, "uid", "1"));
                map.put("timestamp", timeStamp2 + "");
//        map.put("secretKey","C78266CF1A8848B48ECF96C0BD5EE465");
                params.put("sign", PddClient.getSign4(map, "C78266CF1A8848B48ECF96C0BD5EE465"));
                intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", "肯德基");
                intent.putExtra("url", "https://ot.jfshou.cn/api/entrance?" + params.toString());
                startActivity(intent);
                break;
            case R.id.icon_life_maidanglao:
                break;
            case R.id.icon_life_meituan1:
                getInfo("3");
                break;
            case R.id.icon_life_meituan:
                getInfo("1");
                break;
            case R.id.icon_life_sp:
                intent = new Intent(context, WebViewActivity3.class);
                intent.putExtra("title", "视频充值");
                long timeSftamp = System.currentTimeMillis();
                String signstrd = "agentId=405&machineCode=" + SPUtils.getStringData(context, "uid", "1") + "&timestamp=" + timeSftamp;
                String singstrsd = signstrd + "&secretKey=Pe7HRBJYwQfpxCb3s5TGW3hXeWxhXKpH";
                String signs = md5(singstrsd);
                intent.putExtra("url", "http://tq.jfshou.cn/seller/videoApp/classify?machineCode=" + SPUtils.getStringData(context, "uid", "1") + "&agentId=405&timestamp=" + timeSftamp + "&sign=" + signs);
                startActivity(intent);
                break;
            case R.id.icon_super_a:
                openActivity(PddActivity.class);
                break;
            case R.id.icon_super_b:
                openActivity(JdActivity.class);
                break;
            case R.id.icon_super_c:
                openActivity(WphActivity.class);
                break;
            case R.id.icon_super_d:
                openActivity(SuningHomeActivity.class);
                break;
            case R.id.icon_taobao_a:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "9");
                startActivity(intent);
                break;
            case R.id.icon_taobao_b:
                intent = new Intent(context, WebViewActivity3.class);
                intent.putExtra("title", "天猫超市");
                intent.putExtra("url", "https://chaoshi.tmall.com/?targetPage=index");
                startActivity(intent);
                break;
            case R.id.icon_taobao_c:
                intent = new Intent(context, WebViewActivity3.class);
                intent.putExtra("title", "天猫美妆");
                intent.putExtra("url", "https://meizhuang.tmall.com");
                startActivity(intent);
                break;
            case R.id.icon_taobao_d:
                intent = new Intent(context, WebViewActivity3.class);
                intent.putExtra("title", "天猫国际");
                intent.putExtra("url", "https://pages.tmall.com/wow/jinkou/act/zhiyingchaoshi?from=zebra:offline");
                startActivity(intent);
                break;
            case R.id.icon_taobao_e:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "20");
                startActivity(intent);
                break;
            case R.id.icon_taobao_f:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "5");
                startActivity(intent);
                break;
            case R.id.icon_taobao_g:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "22");
                startActivity(intent);
                break;
            case R.id.icon_taobao_h:
                if ("".equals(SPUtils.getStringData(context, "token", ""))) {
                    openActivity(WelActivity.class);
                    return;
                }
                openActivity(ZeroBuyActivity.class);
                break;
            case R.id.icon_taobao_i:
                openActivity(PHBActivity.class);
                break;
            case R.id.icon_taobao_j:
                openActivity(PinPaiFragment.class);
                break;
            case R.id.icon_taobao_k:
                openActivity(GuoyedanActivity.class);
                break;
            case R.id.icon_taobao_l:
                intent = new Intent(getActivity(), GaoyongActivity.class);
                intent.putExtra("url", "/api/Zhetaoke/getBaodanGoods");
                intent.putExtra("title", "捡漏神单");
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.icon_taobao_m:
                break;
            case R.id.icon_life_h:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "23");
                startActivity(intent);
                break;
            case R.id.icon_taobao_n:
                intent = new Intent(context, WebViewActivity3.class);
                intent.putExtra("title", "飞猪旅行");
                intent.putExtra("url", "https://h5.m.taobao.com/trip/wx-random-door/index/index.html");
                startActivity(intent);
                break;
            case R.id.icon_taobao_o:
                intent = new Intent(getActivity(), BaoYouActivity.class);
                intent.putExtra("type", "4");
                startActivity(intent);
                break;
            case R.id.icon_taobao_p:
                Bundle bundle = new Bundle();
                bundle.putString("id", "0");
                openActivity(DouActivity.class, bundle);
                break;
            case R.id.icon_life_a:
                openActivity(ElemeActivity.class);
                break;
            case R.id.icon_life_b:
                Acp.getInstance(context).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        openActivity(MerchanthomeActivity.class);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        showToast("需要您的定位权限");
                    }
                });
                break;
            case R.id.icon_life_c:
                intent = new Intent(context, WebViewActivity3.class);
                intent.putExtra("title", "生活券");
                long timeStamp = System.currentTimeMillis();
                String signstr = "agentId=405&machineCode=" + SPUtils.getStringData(context, "uid", "1") + "&timestamp=" + timeStamp;
                String singstrs = signstr + "&secretKey=Pe7HRBJYwQfpxCb3s5TGW3hXeWxhXKpH";
                String sign = md5(singstrs);
                intent.putExtra("url", "http://tq.jfshou.cn/seller/app/classify?machineCode=" + SPUtils.getStringData(context, "uid", "1") + "&agentId=405&timestamp=" + timeStamp + "&sign=" + sign);
                startActivity(intent);
                break;
            case R.id.icon_life_d:
                intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", "充值");
                intent.putExtra("url", "http://app.yangkeduo.com/deposit.html");
                startActivity(intent);
                break;
            case R.id.icon_life_e:
                if ("".equals(SPUtils.getStringData(context, "token", ""))) {
                    openActivity(WelActivity.class);
                    return;
                }
                Acp.getInstance(context).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        openActivity(JiayoulistActivity.class);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        showToast("加油站需要您的定位权限");
                    }
                });
                break;
            case R.id.icon_life_f:
                intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", "洗车");
                intent.putExtra("url", "https://wash.chengniu.com/wash/#/home?systemCode=CPH5&activityChannel=zxyd&orderChannel=59");
                startActivity(intent);
                break;
            case R.id.icon_life_g:
                if ("".equals(SPUtils.getStringData(context, "token", ""))) {
                    openActivity(WelActivity.class);
                    return;
                }
                intent = new Intent(context, WebViewActivity3.class);
                intent.putExtra("title", "美团酒店");
                intent.putExtra("url", "https://runion.meituan.com/url?a=1&key=ebf5da1e1cd2073df1e538618fca245c348&url=https://i.meituan.com/awp/h5/hotel/search/search.html?cevent=imt%2Fhomepage%2Fcategory1%2F20&sid=hkx_" + SPUtils.getStringData(context, "uid", ""));
                startActivity(intent);
                break;
            case R.id.icon_life_cd:
                Acp.getInstance(context).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .build(), new AcpListener() {
                    @Override
                    public void onGranted() {
                        getToken();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        showToast("充电站需要您的定位和相机权限");
                    }
                });
                break;
        }
    }

    private void toCha() {
        RequestParams params = new RequestParams();
        params.put("share_code", "127SIwqxj");
        long timeStamp2 = System.currentTimeMillis();
        params.put("user_id", SPUtils.getStringData(context, "uid", "1"));
        params.put("timestamp", timeStamp2);
//        params.put("secretKey","C78266CF1A8848B48ECF96C0BD5EE465");
        Map<String, String> map = new HashMap<>();
        map.put("shareCode", "127SIwqxj");
        map.put("userId", SPUtils.getStringData(context, "uid", "1"));
        map.put("timestamp", timeStamp2 + "");
//        map.put("secretKey","C78266CF1A8848B48ECF96C0BD5EE465");
        params.put("sign", PddClient.getSign4(map, "C78266CF1A8848B48ECF96C0BD5EE465"));
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", "奈雪的茶");
        intent.putExtra("url", "https://ot.jfshou.cn/api/nayuki/entrance?" + params.toString());
        startActivity(intent);
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
