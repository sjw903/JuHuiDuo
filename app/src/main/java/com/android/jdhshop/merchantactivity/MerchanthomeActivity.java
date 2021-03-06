package com.android.jdhshop.merchantactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.jdhshop.common.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.bean.BannerBean;
import com.android.jdhshop.bean.Response;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.onOKJsonHttpResponseHandler;
import com.android.jdhshop.merchantadapter.MerchanthomeIconAdapter;
import com.android.jdhshop.merchantadapter.MerchantlistAdapter;
import com.android.jdhshop.merchantbean.MerchantGroupbean;
import com.android.jdhshop.merchantbean.Merchantlistbean;
import com.android.jdhshop.utils.CornerTransform;
import com.android.jdhshop.utils.StickHeaderDecoration;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocatedCity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MerchanthomeActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView typerecy,merchantrecy;
    private TextView tv_selectaddress;
    private Banner banner;
    private EditText et_seek;
    private MerchanthomeIconAdapter merchanthomeIconAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private RadioGroup page_rg;

    //??????AMapLocationClient?????????
    AMapLocationClient mLocationClient = null;
    // ??????AMapLocationClientOption??????
    public AMapLocationClientOption mLocationOption = null;
    public boolean isdw = true;
    private int page = 1;
    private LinearLayout headLayout;

    private MerchantlistAdapter merchantlistAdapter;


    @Override
    protected void initUI() {
        setContentView(R.layout.activity_merchanthome);
        headLayout= (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_head_merchant,null);
//        sortHeader=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.sort_header,null);
        typerecy = headLayout.findViewById(R.id.merchanthome_typerecy);
        // 1.???????????????????????????
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(
                2, 5, PagerGridLayoutManager.HORIZONTAL);
        layoutManager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {
            }

            @Override
            public void onPageSelect(int pageIndex) {
                page_rg.check(pageIndex);
            }
        });
        page_rg = headLayout.findViewById(R.id.page_rg);
        layoutManager.setChangeSelectInScrolling(true);
        typerecy.setLayoutManager(layoutManager);
// 2.????????????????????????
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(typerecy);
        merchanthomeIconAdapter = new MerchanthomeIconAdapter(this,R.layout.service_home_grid_item,iconlist);
        typerecy.setAdapter(merchanthomeIconAdapter);
        merchantrecy = findViewById(R.id.merchanthome_merchantrecy);
        banner = headLayout.findViewById(R.id.merchanthome_banner);
        smartRefreshLayout = findViewById(R.id.refresh_layout);
        et_seek = findViewById(R.id.merchanthome_etseek);
        tv_selectaddress = findViewById(R.id.merchanthome_selectaddress);
        tv_selectaddress.setOnClickListener(this);
        findViewById(R.id.txt_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartRefreshLayout.autoRefresh();
            }
        });
//        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                page=1;
                getmerchantlist();
                //refreshlayout.finishRefresh(2000/*,false*/);//??????false??????????????????
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page ++;
                getmerchantlist();

                //refreshlayout.finishLoadMore(2000/*,false*/);//??????false??????????????????

            }
        });
        getmerchantlist();

    }

    @Override
    protected void initData() {
//        merchanthomeIconAdapter = new MerchanthomeIconAdapter(this,R.layout.service_home_grid_item,iconlist);
//        typerecy.setLayoutManager(new GridLayoutManager(this,5));
//        typerecy.setAdapter(merchanthomeIconAdapter);
        merchanthomeIconAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(MerchanthomeActivity.this,MerchantlistActivity.class);
                intent.putExtra("title",iconlist.get(position).name);
                intent.putExtra("groupid",iconlist.get(position).cat_id);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
//        getauthlist();
        getBanner();
        geticon();
        getPosition();
        merchantlistAdapter = new MerchantlistAdapter(merchantlistbeans);
        merchantrecy.setLayoutManager(new LinearLayoutManager(this));
//        merchantrecy.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        merchantlistAdapter.addHeaderView(headLayout);
//        merchantlistAdapter.addHeaderView(sortHeader);
        merchantrecy.setAdapter(merchantlistAdapter);
        merchantrecy.addItemDecoration(new StickHeaderDecoration(merchantrecy));
        merchantlistAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==0){
                    return;
                }
                Intent intent = new Intent(MerchanthomeActivity.this,MerchantmsgActivity.class);
                intent.putExtra("msgid",merchantlistbeans.get(position).merchant_id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initListener() {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }
    public void showcitysele(){
        List<HotCity> hotCities = new ArrayList();
        hotCities.add(new HotCity("??????", "??????", "101010100")); //code???????????????
        hotCities.add(new HotCity("??????", "??????", "101020100"));
        hotCities.add(new HotCity("??????", "??????", "101280101"));
        hotCities.add(new HotCity("??????", "??????", "101280601"));
        hotCities.add(new HotCity("??????", "??????", "101210101"));
        //APP??????????????????????????????null???????????????????????????
        CityPicker.from(this) //activity??????fragment
                .enableAnimation(true)	//??????????????????????????????
                //.setAnimationStyle(anim)	//???????????????
                .setLocatedCity(new LocatedCity(latLongString, city, ""))
                .setHotCities(hotCities)	//??????????????????
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        tv_selectaddress.setText( data.getName() );
                        latLongString = data.getName();
                        Constants.citys = data.getName();
//                        page = 1;
//                        moreLists.clear();
//                        getNearByList(page+"");
//                        Constant.citys = data.getName();
//                                    Toast.makeText(getActivity(), data.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(){
                        Toast.makeText(MerchanthomeActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLocate() {
                        //?????????????????????APP???????????????????????????????????????
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //??????????????????????????????
//                                            CityPicker.getInstance()
//                                                    .locateComplete(new LocatedCity("??????", "??????", "101280601"), LocateState.SUCCESS);
                            }
                        }, 3000);
                    }
                })
                .show();
    }
    //???????????????????????????
    String latLongString = "?????????",city;
    private void getAddress(double[] data) {
        List<Address> addList = null;
        Geocoder ge = new Geocoder(MerchanthomeActivity.this);
        try {
            addList = ge.getFromLocation(data[0], data[1], 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address ad = addList.get(i);
                latLongString = ad.getLocality();
                Constants.citys = latLongString;
                city = ad.getAdminArea();
                tv_selectaddress.setText(latLongString );
                //????????????list
//                Toast.makeText( PoeticproseMsgActivity.this,latLongString,Toast.LENGTH_SHORT ).show();
            }
        }else{
//            getNearByList(page+"");
        }
    }
    public void getPosition() {
        //???????????????
        mLocationClient = new AMapLocationClient(MerchanthomeActivity.this);
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
    // ???????????????????????????
    String longitudestr;
    String latitudestr;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation == null) {
//                Log.i(TAG, "amapLocation is null!");
                return;
            }
            if (amapLocation.getErrorCode() != 0) {
//                Log.i(TAG, "amapLocation has exception errorCode:" + amapLocation.getErrorCode());
                return;
            }
            Double longitude = amapLocation.getLongitude();//????????????
            Double latitude = amapLocation.getLatitude();//????????????
            longitudestr = String.valueOf(longitude);
            latitudestr = String.valueOf(latitude);
//            Content.jwd = new double[]{ Content.wds, Content.jds};
            Constants.jds = longitude;
            Constants.wds = latitude;
            Log.e("jwd",longitudestr+"--"+latitudestr);
            if (isdw) {
                getAddress( new double[]{latitude, longitude} );
                isdw = !isdw;
            }
            page=1;
            getmerchantlist();

//            if (Content.wds == 0.0&& Content.jds == 0.0){
//
//            }else {
//
//            }
//            Log.i(TAG, "longitude:" + longitude + ",latitude???" + latitude+"\n"+"????????????"+Content.jds+","+Content.wds);
        }
    };




    public void setbanner(){
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                if (context != null && !((Activity) context).isDestroyed())
                    Glide.with(context).load(path).asBitmap().skipMemoryCache(false).dontAnimate().error(R.drawable.no_banner).transform(new CornerTransform(context, 10)).into(imageView);
            }

        });
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setDelayTime(5000);
        banner.start();

    }
    private List<BannerBean> images = new ArrayList<>();
    private void getBanner() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("cat_id", 7);
        HttpUtils.post(Constants.GET_BANNER, MerchanthomeActivity.this,requestParams, new onOKJsonHttpResponseHandler<BannerBean>(new TypeToken<Response<BannerBean>>() {
        }) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Response<BannerBean> datas) {
                if (datas.isSuccess()) {
                    images.clear();
                    images.addAll(datas.getData().getList());
                    List<String> strings = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        strings.add(Constants.APP_IP + images.get(i).getImg());
                    }
                    banner.update(strings);
                    setbanner();
                } else {
//                    showToast(datas.getMsg());
                }
            }
        });
    }
    private List<MerchantGroupbean> iconlist = new ArrayList<>();
    private void geticon() {
        RequestParams requestParams = new RequestParams();
        HttpUtils.post(Constants.getMerchantGroup,  MerchanthomeActivity.this,requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dsfasd", responseString);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd", responseString);
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            iconlist.add(new Gson().fromJson(array.getJSONObject(i).toString(), MerchantGroupbean.class));
                        }
                        int count = (iconlist.size() - 1) / 10 + 1;
                        for (int i = 0; i < count; i++) {
                            RadioButton rb = (RadioButton) LayoutInflater.from(MerchanthomeActivity.this).inflate(R.layout.item_page_group, null);
                            rb.setId(i);
                            if (i == 0) {
                                rb.setChecked(true);
                            }
                            page_rg.addView(rb);
                        }

                        merchanthomeIconAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public List<Merchantlistbean> merchantlistbeans = new ArrayList<>();
    private void getmerchantlist() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("token",SPUtils.getStringData(this, "token", ""));
        requestParams.put("page",page);
        requestParams.put("search",et_seek.getText().toString().trim());
//        requestParams.put("lng_lat",Constants.jds+","+Constants.wds);
        requestParams.put("per","10");
        HttpUtils.post(Constants.getMerchantList, MerchanthomeActivity.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LogUtils.d("dsfasd", responseString);
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.d("dsfasd", responseString);
                if (smartRefreshLayout!=null){
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
                try {
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        if(page==1){
                            merchantlistbeans.clear();
                            merchantlistbeans.add(new Merchantlistbean());
                        }
                        for (int i = 0; i < array.length(); i++) {
                            merchantlistbeans.add(new Gson().fromJson(array.getJSONObject(i).toString(), Merchantlistbean.class));
                        }
                        merchantlistAdapter.notifyDataSetChanged();
                    } else {
                        showToast(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


//    //????????????
//    public List<Authbean> authbeanList = new ArrayList<>();
//    private void getauthlist() {
//        RequestParams requestParams = new RequestParams();
//        HttpUtils.post(Constants.getMerchantAuth, requestParams, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//            }
//
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                LogUtils.d("dsfasd", responseString);
//                try {
//                    JSONObject object = new JSONObject(responseString);
//                    if ("0".equals(object.getString("code"))) {
//                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
//                        for (int i = 0; i < array.length(); i++) {
//                            authbeanList.add(new Gson().fromJson(array.getJSONObject(i).toString(), Authbean.class));
//                        }
//                    } else {
//                        showToast(object.getString("msg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.merchanthome_selectaddress:
                showcitysele();
                break;
        }
    }
}
