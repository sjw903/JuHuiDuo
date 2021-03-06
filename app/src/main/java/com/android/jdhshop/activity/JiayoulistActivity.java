package com.android.jdhshop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.jdhshop.common.LogUtils;
import com.google.gson.Gson;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.JiayoulistAdapter;
import com.android.jdhshop.adapter.JiayouorderAdapter;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.Jiayoulistbean;
import com.android.jdhshop.bean.Jiayouorderbean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.login.WelActivity;
import com.android.jdhshop.my.RechargeActivity;
import com.android.jdhshop.utils.GpsUtil;
import com.android.jdhshop.utils.RecyclerViewSpacesItemDecoration;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * ?????????
 * */
public class JiayoulistActivity extends BaseActivity implements OnClickListener{

    private LinearLayout ly_left,ly_center,ly_right,ly_right2;
    private RecyclerView recyclerView,recyclerView2;
    private SmartRefreshLayout smartRefreshLayout;
    private JiayoulistAdapter jiayoulistAdapter;
    private NiceSpinner leftspinner,centspinner,rightspinner,rightspinner2;
    private LinearLayout ly_back;
    private TextView tv_goorder;
    private TextView tv1,tv2;
    private ImageView img1,img2;
    private LinearLayout ly1,ly2;
    private LinearLayout ly_title;

    String[] leftstr = {"??????","?????????","?????????","??????"};
    String[] centstr = {"92??????","95??????","98??????","0??????"};
    String[] rightstr = {"??????","1??????","3??????","5??????","10??????","20??????","30??????"};
    String[] rightstr2 = {"????????????","????????????"};
    List<String> leftlist = new ArrayList<>(  );
    List<String> centtlist = new ArrayList<>(  );
    List<String> rightlist = new ArrayList<>(  );
    List<String> rightlist2 = new ArrayList<>(  );

    //??????AMapLocationClient?????????
    AMapLocationClient mLocationClient = null;
    // ??????AMapLocationClientOption??????
    public AMapLocationClientOption mLocationOption = null;
    private String phone;
    JiayouorderAdapter jiayouorderAdapter;
    boolean isfist = true;
    private int page = 1;
    private int page2 = 1;
    private String juli = "";
    private String yhbm = "92";
    private String leixin = "";

    private int type = 1;


    @Override
    protected void initUI() {
        setStatusBar(Color.parseColor("#FEA04D"));
        setContentView(R.layout.activity_jiayoulist);
        if("".equals(SPUtils.getStringData(this,"token",""))){
            T.showShort(this, "????????????");
            openActivity(WelActivity.class);
            finish();
        }
        initview();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    public void initview(){
        if(CaiNiaoApplication.getUserInfoBean()!=null)
        phone = CaiNiaoApplication.getUserInfoBean().user_msg.phone;
        ly_left = findViewById( R.id.jiayou_left );
        ly_center = findViewById( R.id.jiayou_center );
        ly_right = findViewById( R.id.jiayou_right );
        ly_right2 = findViewById( R.id.jiayou_right2 );
        recyclerView = findViewById( R.id.jiayou_recy );
        smartRefreshLayout = findViewById( R.id.refresh_layout );
        leftspinner = findViewById( R.id.jiayou_leftspinner );
        centspinner = findViewById( R.id.jiayou_centspinner );
        rightspinner = findViewById( R.id.jiayou_rightspinner );
        rightspinner2 = findViewById( R.id.jiayou_rightspinner2 );
        ly_back = findViewById( R.id.jiayou_lyback );
        tv_goorder = findViewById( R.id.jiayou_goorder );
        recyclerView2 = findViewById( R.id.jiayou_recy2 );
        tv1 = findViewById( R.id.jiayou_tv1 );
        tv2 = findViewById( R.id.jiayou_tv2 );
        img1 = findViewById( R.id.jiayou_img1 );
        img2 = findViewById( R.id.jiayou_img2 );
        ly1 = findViewById( R.id.jiayou_ly1 );
        ly2 = findViewById( R.id.jiayou_ly2 );
        ly_title = findViewById( R.id.jiayou_title );
        ly1.setOnClickListener( this );
        ly2.setOnClickListener( this );
        ly_back.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        tv_goorder.setOnClickListener( new OnClickListener() {//??????
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent( JiayoulistActivity.this,JiayouorderActivity.class );
//                startActivity( intent );
//                Toast.makeText( JiayoulistActivity.this,"????????????",Toast.LENGTH_SHORT ).show();
            }
        } );
        leftspinner.setBackgroundColor( Color.parseColor( "#ffffff" ) );
        centspinner.setBackgroundColor( Color.parseColor( "#ffffff" ) );
        rightspinner.setBackgroundColor( Color.parseColor( "#ffffff" ) );
        rightspinner2.setBackgroundColor( Color.parseColor( "#ffffff" ) );

        for (int i = 0;i<leftstr.length;i++){
            leftlist.add( leftstr[i] );
        }
        for (int i = 0;i<centstr.length;i++){
            centtlist.add( centstr[i] );
        }
        for (int i = 0;i<rightstr.length;i++){
            rightlist.add( rightstr[i] );
        }
        for (int i = 0;i<rightstr2.length;i++){
            rightlist2.add( rightstr2[i] );
        }
        leftspinner.attachDataSource( leftlist );
        centspinner.attachDataSource( centtlist );
        rightspinner.attachDataSource( rightlist );
        rightspinner2.attachDataSource( rightlist2 );

        leftspinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (leftstr[i].equals( "?????????" )){
                    leixin = "1";
                }else if (leftstr[i].equals( "?????????" )){
                    leixin = "2";
                }else if (leftstr[i].equals( "??????" )){
                    leixin = "3";
                }else if (leftstr[i].equals( "??????" )){
                    leixin = "";
                }
                page = 1;
                jiayoulistbeans.clear();
                getlist();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
        centspinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (centstr[i].equals( "95??????" )){
                    yhbm = "95";
                }else if (centstr[i].equals( "92??????" )){
                    yhbm = "92";
                }else if (centstr[i].equals( "98??????" )){
                    yhbm = "98";
                }else if (centstr[i].equals( "0??????" )){
                    yhbm = "0";
                }

                page = 1;
                jiayoulistbeans.clear();
                getlist();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
        rightspinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (rightstr[i].equals( "1??????" )){
                    juli = "1000";
                }else if (rightstr[i].equals( "3??????" )){
                    juli = "3000";
                }else if (rightstr[i].equals( "5??????" )){
                    juli = "5000";
                }else if (rightstr[i].equals( "10??????" )){
                    juli = "10000";
                }else if(rightstr[i].equals("20??????")){
                    juli = "20000";
                }else if(rightstr[i].equals("30??????")){
                    juli = "30000";
                }else if(rightstr[i].equals("??????")){
                    juli = "";
                }
                page = 1;
                jiayoulistbeans.clear();
                getlist();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
        rightspinner2.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                page = 1;
                jiayoulistbeans.clear();
                getlist();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
        jiayouorderAdapter = new JiayouorderAdapter( JiayoulistActivity.this,jiayoulistbeans2 );
        recyclerView2.addItemDecoration( new RecyclerViewSpacesItemDecoration( 8,15,8,15 ) );
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager( JiayoulistActivity.this,1 );
        gridLayoutManager2.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView2.setLayoutManager( gridLayoutManager2 );
        recyclerView2.setAdapter( jiayouorderAdapter );
        jiayouorderAdapter.setsubClickListener( new JiayouorderAdapter.SubClickListener() {
            @Override
            public void OntopicClickListener(View v, String detail, int posit) {
                if (detail.equals( "help" )){
                    showTwo();
                }
            }
        } );


        jiayoulistAdapter = new JiayoulistAdapter( JiayoulistActivity.this,jiayoulistbeans );
        recyclerView.addItemDecoration( new RecyclerViewSpacesItemDecoration( 8,15,8,15 ) );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( JiayoulistActivity.this,1 );
        gridLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( gridLayoutManager );
        recyclerView.setAdapter( jiayoulistAdapter );
        jiayoulistAdapter.setsubClickListener( new JiayoulistAdapter.SubClickListener() {
            @Override
            public void OntopicClickListener(View v, String detail, int posit) {
                if (detail.equals( "jy" )){//??????
                    Intent intent = new Intent( JiayoulistActivity.this,JiayoumsgActivity.class );
                    intent.putExtra( "id",jiayoulistbeans.get( posit).gas_id );
                    intent.putExtra( "address",jiayoulistbeans.get( posit ).gasaddress );
                    intent.putExtra( "img",jiayoulistbeans.get( posit ).gaslogosmall );
                    startActivity( intent );
                }else if (detail.equals( "dh" )){//??????
                    Constants.lat =  jiayoulistbeans.get( posit ).gasaddreslatitude ;
                    Constants.lng = jiayoulistbeans.get( posit ).gasaddresslongitude ;
                    goodalertdialog();
                }
            }
        } );
        getPosition();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                if (type == 1){
                    jiayoulistbeans.clear();
                    page = 1;
                    getlist();
                }else if (type == 2){
                    jiayoulistbeans2.clear();

                    page2 = 1;

                    getlist2();
                }


                //refreshlayout.finishRefresh(2000/*,false*/);//??????false??????????????????
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (type == 1){
                    page++;
                    getlist();
                }else if (type ==2)
                {
                    page2++;

                    getlist2();
                }


                //refreshlayout.finishLoadMore(2000/*,false*/);//??????false??????????????????

            }
        });

    }
    public void getPosition() {
        //???????????????
        mLocationClient = new AMapLocationClient(JiayoulistActivity.this);
        // ????????????????????????
        mLocationClient.setLocationListener(mLocationListener);
        // ?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        // ?????????????????????AMapLocationMode.Hight_Accuracy?????????????????????
        mLocationOption.setLocationMode( AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //??????????????????,????????????,?????????2000ms
        mLocationOption.setInterval(60000*5);
        // ??????????????????????????? //??????????????????false???
        mLocationOption.setOnceLocation(false);
        mLocationOption.setOnceLocationLatest(false);
        //??????????????????????????????????????????
        mLocationClient.setLocationOption(mLocationOption);
        // ????????????
        mLocationClient.startLocation();
    }
    String jytoken = "";
    public List<Jiayoulistbean> jiayoulistbeans = new ArrayList<>(  );
    private void getlist() {
        RequestParams requestParams = new RequestParams();
        requestParams.put( "phone",phone);
        requestParams.put( "lng_lat", GpsUtil.toGCJ02Point( longitudestr, latitudestr)[0]+","+GpsUtil.toGCJ02Point( longitudestr, latitudestr)[1]);
        requestParams.put("page", page);
        requestParams.put("per", "10");
        requestParams.put( "cond",2 );
        if(!"".equals(leixin)){
            requestParams.put( "gasType",leixin );
        }
        requestParams.put( "oilNo",yhbm );
//        requestParams.put( "token",jytoken );



        requestParams.put( "distinct",juli );
        LogUtils.d("1eqwew",requestParams.toString());
        Log.e( "cont","phone"+phone+";lng_lat"+ GpsUtil.toGCJ02Point( longitudestr, latitudestr)[0]+","+GpsUtil.toGCJ02Point( longitudestr, latitudestr)[1]);
        HttpUtils.post( Constants.index, JiayoulistActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
                LogUtils.d("dfasdf",responseString);
            }

            @Override
            public void onStart() {
                showLoadingDialog();
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeLoadingDialog();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONObject jsob = object.getJSONObject("data");
                        JSONArray array = jsob.getJSONArray( "list" );
                        for (int i = 0; i < array.length(); i++) {
                            jiayoulistbeans.add(new Gson().fromJson(array.getJSONObject(i).toString(), Jiayoulistbean.class));
                        }
                        jiayoulistAdapter.notifyDataSetChanged();
                    }else{
                        jiayoulistAdapter.notifyDataSetChanged();
                        Toast.makeText( JiayoulistActivity.this,object.getString( "msg" ),Toast.LENGTH_SHORT ).show();
//                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    // ???????????????????????????
    Double longitudestr=0.0;
    Double latitudestr=0.0;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation == null) {
//                Log.i(TAG, "amapLocation is null!");
                return;
            }
            if (amapLocation.getErrorCode() != 0) {
//                ToastUtils.showLongToast(getComeActivity(),"??????????????????????????????????????????????????????");
                return;
            }
            Double longitude = amapLocation.getLongitude();//????????????
            Double latitude = amapLocation.getLatitude();//????????????
            longitudestr = longitude;
            latitudestr = latitude;
//            Content.jwd = new double[]{ Content.wds, Content.jds};
//            Constant.jds = longitude;
//            Constant.wds = latitude;
            if (isfist){
                login();
                isfist = !isfist;
                getlist();
            }

//            if (Content.wds == 0.0&& Content.jds == 0.0){
//
//            }else {
//
//            }
//            Log.i(TAG, "longitude:" + longitude + ",latitude???" + latitude+"\n"+"????????????"+Content.jds+","+Content.wds);
        }
    };

    private void login() {
        RequestParams requestParams = new RequestParams();
        requestParams.put( "phone",phone);
        HttpUtils.post( Constants.empower,JiayoulistActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
//                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                closeLoadingDialog();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONObject jsob = object.getJSONObject("data");
                        if (jsob.getString( "code" ).equals( "200" )){
                            String token = jsob.getJSONObject( "result" ).getString( "token" );
                            Log.e( "token",token );
                            jytoken = token;
                            SharedPreferences sharedPreferences=getSharedPreferences("STOREINFO", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString( "jytoken",token );
                            editor.commit();
                            getlist();
                            getlist2();
                        }
                    }else{
                        Toast.makeText( JiayoulistActivity.this,object.getString( "msg" ),Toast.LENGTH_SHORT ).show();
//                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    public void goodalertdialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(this);

        View v = inflater.inflate(R.layout.item_dialog, null);
        dialog.setView(v);
        dialog.show();
        TextView gd =  v.findViewById(R.id.item_dialoggd);
        TextView bd =  v.findViewById(R.id.item_dialogbd);
        //builer.setView(v);//??????????????????builer.setView(v)??????????????????????????????title???button??????????????????
        gd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isAvilible(JiayoulistActivity.this,"com.autonavi.minimap")) {
//                    /**
//                     * ????????????App????????????
//                     * sourceApplication ?????? ????????????????????????????????? amap
//                     * poiname           ????????? POI ??????
//                     * dev               ?????? ????????????(0:lat ??? lon ?????????????????????,?????????????????????; 1:??????????????????)
//                     * style             ?????? ????????????(0 ?????????; 1 ?????????; 2 ?????????; 3 ???????????????4 ???????????????5 ??????????????????????????????6 ??????????????????????????????7 ????????????????????????8 ?????????????????????????????????))
//                     */
//                    StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=")
//                            .append("yitu8_driver").append("&lat=").append(Double.parseDouble(Constant.lat))
//                            .append("&lon=").append(Double.parseDouble(Constant.lng))
//                            .append("&dev=").append(0)
//                            .append("&style=").append(0);
////        if (!TextUtils.isEmpty(poiname)) {
////            stringBuffer.append("&poiname=").append(poiname);
////        }
//                    Intent intent = new Intent(Intent.ACTION_VIEW,android.net.Uri.parse(stringBuffer.toString()));
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    intent.setPackage("com.autonavi.minimap");
//                    startActivity(intent);
                    toGaoDeRoute(JiayoulistActivity.this, "zxyd"
                            , "", "", "", ""
                            , "", Constants.lat, Constants.lng, ""
                            , "0", "0");

                }else{
                    Toast.makeText(JiayoulistActivity.this, "???????????????????????????", Toast.LENGTH_LONG)
                            .show();
//                    Uri uri = Uri
//                            .parse("market://details?id=com.autonavi.minimap");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
                }

                dialog.dismiss();

            }
        });


        bd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isAvilible(JiayoulistActivity.this,"com.baidu.BaiduMap")) {
                    /**
                     * ?????????????????????????????????
                     * intent = Intent.getIntent("baidumap://map/navi?location=34.264642646862,108.95108518068&type=BLK&src=thirdapp.navi.you
                     * location ????????? location???query??????????????????????????????location????????????query
                     * query    ??????key   ??????
                     * type ??????????????????  BLK:????????????(??????);TIME:????????????(??????);DIS:????????????(??????);FEE:????????????(??????);??????DIS
                     */
                    double x = Double.parseDouble(Constants.lat), y = Double.parseDouble(Constants.lng);
                    double z = sqrt(x * x + y * y) + 0.00002 * sin(y * Math.PI);
                    double theta = atan2(y, x) + 0.000003 * cos(x * Math.PI);
                    double bd_lon = z * cos(theta) + 0.0065;
                    double bd_lat = z * sin(theta) + 0.006;
//                    StringBuffer stringBuffer = new StringBuffer("baidumap://map/navi?location=")
//                            .append(bd_lon).append(",").append(bd_lat).append("&type=TIME");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
//                    intent.setPackage("com.baidu.BaiduMap");
//                    startActivity(intent);
                    toBaiDuDirection(JiayoulistActivity.this, "", bd_lon+","+bd_lat, ""
                            , "", "", ""
                            , "", "", "","");
                }else{
                    Toast.makeText(JiayoulistActivity.this, "???????????????????????????", Toast.LENGTH_LONG)
                            .show();
//                    Uri uri = Uri
//                            .parse("market://details?id=com.baidu.BaiduMap");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
                }
                dialog.dismiss();

            }
        });

    }

    public boolean isAvilible(Context context, String packageName) {
        // ??????packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // ???????????????????????????????????????
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // ??????????????????????????????????????????
        List<String> packageNames = new ArrayList<String>();
        // ???pinfo????????????????????????????????????pName list???
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // ??????packageNames???????????????????????????????????????TRUE?????????FALSE
        return packageNames.contains(packageName);
    }


    /**
     * ?????????????????? URL?????????baidumap://map/direction
     *
     * @param context
     * @param origin             ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *                           origin???destination????????????????????????????????????????????????????????????
     *                           ?????????: 39.9761,116.3282
     *                           ??????????????????:
     *                           latlng:39.9761,116.3282|name:????????? (????????????????????????????????????)
     * @param destination        ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * @param mode               ?????????????????????transit???????????????driving???????????????walking???????????????riding????????????.??????:driving
     * @param region             ??????????????????
     * @param origin_region      ????????????????????????
     * @param destination_region ????????????????????????
     * @param sy                 ??????????????????????????????mode????????????transit?????????????????????????????????
     *                           0???????????????
     *                           2????????????
     *                           3????????????
     *                           4???????????????
     *                           5????????????
     *                           6???????????????
     * @param index              ??????????????????????????????????????????????????????????????????0??????
     * @param target             0 ?????????1 ????????????????????????????????????
     * @param coord_type         ???????????????????????????????????????bd09??????????????????
     *                           ???????????????bd09ll???bd09mc???gcj02???wgs84???
     *                           bd09ll??????????????????????????????
     *                           bd09mc??????????????????????????????
     *                           gcj02???????????????????????????????????????
     *                           wgs84??????gps???????????????
     */
    private static void toBaiDuDirection(Context context, String origin, String destination, String mode
            , String region, String origin_region, String destination_region
            , String sy, String index, String target, String coord_type) {

        StringBuffer sb = new StringBuffer("baidumap://map/direction?mode=").append(mode);

        boolean isHasOrigin = false;
        boolean isHasdestination = false;
        if (!TextUtils.isEmpty(origin)) {
            isHasOrigin = true;
            sb.append("&origin=" + origin);
        }
        if (!TextUtils.isEmpty(destination)) {
            isHasdestination = true;
            sb.append("&destination=" + destination);
        }

        if (!isHasOrigin && !isHasdestination) {
//            MyToast.showShort(context, " ???????????????????????????");
            return;
        }

        if (!TextUtils.isEmpty(region)) {
            sb.append("&region=" + region);
        }
        if (!TextUtils.isEmpty(origin_region)) {
            sb.append("&origin_region=" + origin_region);
        }
        if (!TextUtils.isEmpty(destination_region)) {
            sb.append("&destination_region=" + destination_region);
        }
        if (!TextUtils.isEmpty(sy)) {
            sb.append("&sy=" + sy);
        }
        if (!TextUtils.isEmpty(index)) {
            sb.append("&index=" + index);
        }
        if (!TextUtils.isEmpty(target)) {
            sb.append("&target=" + target);
        }
        if (!TextUtils.isEmpty(coord_type)) {
            sb.append("&coord_type=" + coord_type);
        }


        Intent i1 = new Intent();
        i1.setData( Uri.parse(sb.toString()));
        context.startActivity(i1);
    }
    /**
     * ????????????App???????????????????????? http://lbs.amap.com/api/amap-mobile/guide/android/route
     *
     * @param context
     * @param sourceApplication ?????? ????????????????????????????????? "appName"
     * @param sid
     * @param sla
     * @param slon
     * @param sname
     * @param did
     * @param dlat
     * @param dlon
     * @param dName
     * @param dev               ?????????????????????(0:lat ??? lon ?????????????????????,?????????????????????; 1:??????????????????)
     * @param t                 t = 0????????????= 1????????????= 2????????????= 3????????????= 4????????????= 5??????????????????
     *                          ???????????????V788?????????????????????
     */
    public static void toGaoDeRoute(Context context, String sourceApplication
            , String sid, String sla, String slon, String sname
            , String did, String dlat, String dlon, String dName
            , String dev, String t) {
        StringBuffer stringBuffer = new StringBuffer("androidamap://route/plan?sourceApplication=").append(sourceApplication);
        if (!TextUtils.isEmpty(sid)) {
            stringBuffer.append("&sid=").append(sid);
        }
        if (!TextUtils.isEmpty(sla)) {
            stringBuffer.append("&sla=").append(sla);
        }
        if (!TextUtils.isEmpty(sla)) {
            stringBuffer.append("&sla=").append(sla);
        }
        if (!TextUtils.isEmpty(slon)) {
            stringBuffer.append("&slon=").append(slon);
        }
        if (!TextUtils.isEmpty(sname)) {
            stringBuffer.append("&sname=").append(sname);
        }
        if (!TextUtils.isEmpty(did)) {
            stringBuffer.append("&did=").append(did);
        }
        stringBuffer.append("&dlat=").append(dlat);
        stringBuffer.append("&dlon=").append(dlon);
        stringBuffer.append("&dName=").append(dName);
        stringBuffer.append("&dev=").append(dev);
        stringBuffer.append("&t=").append(t);


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        //?????????Scheme???URI???????????????data
        Uri uri = Uri.parse(stringBuffer.toString());
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.jiayou_ly1:
                recyclerView.setVisibility( View.VISIBLE );
                recyclerView2.setVisibility( View.GONE );
                ly_title.setVisibility( View.VISIBLE );
                type = 1;
                tv1.setTextColor( Color.parseColor( "#FD7700" ) );
                tv2.setTextColor( Color.parseColor( "#989898" ) );
                img1.setImageResource( R.mipmap.icon_nav_oil_delected );
                img2.setImageResource( R.mipmap.icon_nav_order_nomal );
                break;
            case R.id.jiayou_ly2:
                type = 2;
                ly_title.setVisibility( View.GONE );
                recyclerView.setVisibility( View.GONE );
                recyclerView2.setVisibility( View.VISIBLE );
                tv1.setTextColor( Color.parseColor( "#989898" ) );
                tv2.setTextColor( Color.parseColor( "#FD7700" ) );
                img1.setImageResource( R.mipmap.icon_nav_oil_nomal );
                img2.setImageResource( R.mipmap.icon_nav_order_delected );
                break;
        }
    }


    public List<Jiayouorderbean> jiayoulistbeans2 = new ArrayList<>(  );
    private void getlist2() {
        RequestParams requestParams = new RequestParams();
        requestParams.put( "phone",phone);
        requestParams.put("page", page2);
        requestParams.put("per", "10");

        HttpUtils.post( Constants.order,JiayoulistActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }else{

                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONObject jsob = object.getJSONObject("data");
                        JSONArray array = jsob.getJSONArray( "result" );
                        for (int i = 0; i < array.length(); i++) {
                            jiayoulistbeans2.add(new Gson().fromJson(array.getJSONObject(i).toString(), Jiayouorderbean.class));
                        }
                        jiayouorderAdapter.notifyDataSetChanged();
                    }else{
                        jiayouorderAdapter.notifyDataSetChanged();
                        Toast.makeText( JiayoulistActivity.this,object.getString( "msg" ),Toast.LENGTH_SHORT ).show();
//                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    private AlertDialog.Builder builder;
    /**
     * ??????????????? dialog
     */
    private void showTwo() {

        builder = new AlertDialog.Builder(this).setTitle("????????????")
                .setMessage("1,?????????????????????????????????,??????????????????;\n2,??????????????????,????????????????????????:400-0835-999").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: ??????????????????
                        dialogInterface.dismiss();
//                        Toast.makeText(JiayoulistActivity.this, "????????????", Toast.LENGTH_LONG).show();
                    }
                });
        builder.create().show();
    }

}
