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
 * 加油站
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

    String[] leftstr = {"全部","中石油","中石化","壳牌"};
    String[] centstr = {"92号油","95号油","98号油","0号油"};
    String[] rightstr = {"全部","1千米","3千米","5千米","10千米","20千米","30千米"};
    String[] rightstr2 = {"油价升序","距离升序"};
    List<String> leftlist = new ArrayList<>(  );
    List<String> centtlist = new ArrayList<>(  );
    List<String> rightlist = new ArrayList<>(  );
    List<String> rightlist2 = new ArrayList<>(  );

    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    // 声明AMapLocationClientOption对象
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
            T.showShort(this, "请先登录");
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
        tv_goorder.setOnClickListener( new OnClickListener() {//订单
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent( JiayoulistActivity.this,JiayouorderActivity.class );
//                startActivity( intent );
//                Toast.makeText( JiayoulistActivity.this,"敬请期待",Toast.LENGTH_SHORT ).show();
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
                if (leftstr[i].equals( "中石油" )){
                    leixin = "1";
                }else if (leftstr[i].equals( "中石化" )){
                    leixin = "2";
                }else if (leftstr[i].equals( "壳牌" )){
                    leixin = "3";
                }else if (leftstr[i].equals( "全部" )){
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
                if (centstr[i].equals( "95号油" )){
                    yhbm = "95";
                }else if (centstr[i].equals( "92号油" )){
                    yhbm = "92";
                }else if (centstr[i].equals( "98号油" )){
                    yhbm = "98";
                }else if (centstr[i].equals( "0号油" )){
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
                if (rightstr[i].equals( "1千米" )){
                    juli = "1000";
                }else if (rightstr[i].equals( "3千米" )){
                    juli = "3000";
                }else if (rightstr[i].equals( "5千米" )){
                    juli = "5000";
                }else if (rightstr[i].equals( "10千米" )){
                    juli = "10000";
                }else if(rightstr[i].equals("20千米")){
                    juli = "20000";
                }else if(rightstr[i].equals("30千米")){
                    juli = "30000";
                }else if(rightstr[i].equals("全部")){
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
                if (detail.equals( "jy" )){//加油
                    Intent intent = new Intent( JiayoulistActivity.this,JiayoumsgActivity.class );
                    intent.putExtra( "id",jiayoulistbeans.get( posit).gas_id );
                    intent.putExtra( "address",jiayoulistbeans.get( posit ).gasaddress );
                    intent.putExtra( "img",jiayoulistbeans.get( posit ).gaslogosmall );
                    startActivity( intent );
                }else if (detail.equals( "dh" )){//导航
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


                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
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


                //refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败

            }
        });

    }
    public void getPosition() {
        //初始化定位
        mLocationClient = new AMapLocationClient(JiayoulistActivity.this);
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode( AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60000*5);
        // 获取一次定位结果： //该方法默认为false。
        mLocationOption.setOnceLocation(false);
        mLocationOption.setOnceLocationLatest(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
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
    // 声明定位回调监听器
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
//                ToastUtils.showLongToast(getComeActivity(),"定位失败，请检查设备是否开启定位服务");
                return;
            }
            Double longitude = amapLocation.getLongitude();//获取经度
            Double latitude = amapLocation.getLatitude();//获取纬度
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
//            Log.i(TAG, "longitude:" + longitude + ",latitude：" + latitude+"\n"+"记录的："+Content.jds+","+Content.wds);
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
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        gd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isAvilible(JiayoulistActivity.this,"com.autonavi.minimap")) {
//                    /**
//                     * 启动高德App进行导航
//                     * sourceApplication 必填 第三方调用应用名称。如 amap
//                     * poiname           非必填 POI 名称
//                     * dev               必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
//                     * style             必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
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
                    Toast.makeText(JiayoulistActivity.this, "您尚未安装高德地图", Toast.LENGTH_LONG)
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
                     * 打开百度地图导航客户端
                     * intent = Intent.getIntent("baidumap://map/navi?location=34.264642646862,108.95108518068&type=BLK&src=thirdapp.navi.you
                     * location 坐标点 location与query二者必须有一个，当有location时，忽略query
                     * query    搜索key   同上
                     * type 路线规划类型  BLK:躲避拥堵(自驾);TIME:最短时间(自驾);DIS:最短路程(自驾);FEE:少走高速(自驾);默认DIS
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
                    Toast.makeText(JiayoulistActivity.this, "您尚未安装百度地图", Toast.LENGTH_LONG)
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
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 百度路线规划 URL接口：baidumap://map/direction
     *
     * @param context
     * @param origin             起点名称或经纬度，或者可同时提供名称和经纬度，此时经纬度优先级高，将作为导航依据，名称只负责展示
     *                           origin和destination二者至少一个有值（默认值是当前定位地址）
     *                           经纬度: 39.9761,116.3282
     *                           经纬度和名称:
     *                           latlng:39.9761,116.3282|name:中关村 (注意：坐标先纬度，后经度)
     * @param destination        终点名称或经纬度，或者可同时提供名称和经纬度，此时经纬度优先级高，将作为导航依据，名称只负责展示。
     * @param mode               导航模式，可选transit（公交）、driving（驾车）、walking（步行）和riding（骑行）.默认:driving
     * @param region             城市名或县名
     * @param origin_region      起点所在城市或县
     * @param destination_region 终点所在城市或县
     * @param sy                 公交检索策略，只针对mode字段填写transit情况下有效，值为数字。
     *                           0：推荐路线
     *                           2：少换乘
     *                           3：少步行
     *                           4：不坐地铁
     *                           5：时间短
     *                           6：地铁优先
     * @param index              公交结果结果项，只针对公交检索，值为数字，从0开始
     * @param target             0 图区，1 详情，只针对公交检索有效
     * @param coord_type         坐标类型，可选参数，默认为bd09经纬度坐标。
     *                           允许的值为bd09ll、bd09mc、gcj02、wgs84。
     *                           bd09ll表示百度经纬度坐标，
     *                           bd09mc表示百度墨卡托坐标，
     *                           gcj02表示经过国测局加密的坐标，
     *                           wgs84表示gps获取的坐标
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
//            MyToast.showShort(context, " 请输入起点或目的地");
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
     * 启动高德App进行路线规划导航 http://lbs.amap.com/api/amap-mobile/guide/android/route
     *
     * @param context
     * @param sourceApplication 必填 第三方调用应用名称。如 "appName"
     * @param sid
     * @param sla
     * @param slon
     * @param sname
     * @param did
     * @param dlat
     * @param dlon
     * @param dName
     * @param dev               起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param t                 t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
     *                          （骑行仅在V788以上版本支持）
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

        //将功能Scheme以URI的方式传入data
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
     * 两个按钮的 dialog
     */
    private void showTwo() {

        builder = new AlertDialog.Builder(this).setTitle("疑问订单")
                .setMessage("1,订单状态更新可能会延迟,建议稍后查看;\n2,如有退款疑问,您可拨打客服电话:400-0835-999").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
//                        Toast.makeText(JiayoulistActivity.this, "确定按钮", Toast.LENGTH_LONG).show();
                    }
                });
        builder.create().show();
    }

}
