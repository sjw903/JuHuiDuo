package com.android.jdhshop.mall;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.malladapter.MallGoodsCartAdapter;
import com.android.jdhshop.mallbean.BuyCarBean;
import com.android.jdhshop.mallbean.MallGoodsCarListBean;
import com.android.jdhshop.mallbean.Selectbean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 商城购物车
 * */
public class MallGoodsCartActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_alljifen,tv_button;
    private ImageView iv_allcheck;
    private LinearLayout ly_back;
    private ListView listView;
    private MallGoodsCartAdapter jifengoodCartAdapter;
    List<MallGoodsCarListBean> lists;

    private String goodid;
    private String goodsku;
    String token;
    @Override
    protected void initUI() {
       setContentView(R.layout.activity_jifen_good_cart);
        tv_alljifen = findViewById( R.id.jifengoodcart_alljifen );
        tv_button = findViewById( R.id.jifengoodcart_button );
        iv_allcheck = findViewById( R.id.jifengoodcart_allischeck );
        ly_back = findViewById( R.id.jifengoodcart_lyback );
        listView = findViewById( R.id.jifengoodcart_listview );
        tv_button.setOnClickListener( this );
        ly_back.setOnClickListener( this );
        iv_allcheck.setOnClickListener( this );
        token= SPUtils.getStringData(this,"token","");
        getgridviews();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    boolean allischeck = false;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.jifengoodcart_lyback:
                finish();
                break;
            case R.id.jifengoodcart_button://提交

                String all = tv_alljifen.getText().toString().replace( "元","" );
                if (!all.equals( "0" )) {
                    BuyCarBean buyCarBean=null;
                    List<BuyCarBean> buys=new ArrayList<>();
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get( i ).ischeck) {
                            buyCarBean=new BuyCarBean();
                            buyCarBean.setGoods_id(lists.get(i).id);//购物车id充当商品id
                            buyCarBean.setGoods_name(lists.get(i).goods_name);
                            buyCarBean.setImg(lists.get(i).img);
                            buyCarBean.setMerchant_id(lists.get(i).merchant_id);
                            buyCarBean.setNum(lists.get(i).goods_num+"");
                            buyCarBean.setOld_price(lists.get(i).old_price);
                            buyCarBean.setPrice(lists.get(i).price);
                            buyCarBean.setPostage(lists.get(i).postage);
                            List<Selectbean> skuList=new ArrayList<>();
                            try {
                                JSONArray array=new JSONArray(lists.get(i).sku_arr);
                                for(int n=0;n<array.length();n++)
                                    skuList.add(new Selectbean(array.getJSONObject(n).getString("attribute_id"),array.getJSONObject(n).getString("value")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            buyCarBean.setSelectbeans(skuList);
                            buys.add(buyCarBean);
                        }
                    }
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("buyCarBean", (Serializable) buys);
                    openActivity(BuyGoodsActivity.class,bundle);
                }else{
                    Toast.makeText( MallGoodsCartActivity.this,"请选择商品",Toast.LENGTH_SHORT ).show();
                }
                break;
            case R.id.jifengoodcart_allischeck://全选
                if (allischeck){
                    isall();
                    iv_allcheck.setImageResource( R.drawable.unsel_check );
                    allischeck = !allischeck;
                }else{
                    isall();
                    iv_allcheck.setImageResource( R.drawable.sel_check );
                    allischeck = !allischeck;
                }
                break;


        }
    }

    public void getgridviews(){
        lists = new ArrayList<>(  );
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("token", token).build();
        Request request = new Request.Builder().url(Constants.APP_IP+"/api/Shopcart/getShopcartList").post( body ).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("'dsfasd",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject object = jsonObject.getJSONObject( "data" );
                    JSONArray imglist = object.getJSONArray( "list" );
                    int size = imglist.length();
                    for (int i = 0;i<size;i++){
                        JSONObject obj = (JSONObject) imglist.get(i);
                        MallGoodsCarListBean jifenGoodcartlistbean = new MallGoodsCarListBean();
                        jifenGoodcartlistbean.id = obj.getString( "id" );
                        jifenGoodcartlistbean.user_id = obj.getString( "user_id" );
//                        jifenGoodcartlistbean.merchant_id = obj.getString( "merchant_id" );
                        jifenGoodcartlistbean.goods_id = obj.getString( "goods_id" );
                        jifenGoodcartlistbean.goods_num = obj.getString( "goods_num" );
                        jifenGoodcartlistbean.goods_name = obj.getString( "goods_name" );
                        jifenGoodcartlistbean.img = obj.getString( "img" );
                        jifenGoodcartlistbean.tmp_img = obj.getString( "tmp_img" );
                        jifenGoodcartlistbean.old_price = obj.getString( "old_price" );
                        jifenGoodcartlistbean.price = obj.getString( "price" );
                        jifenGoodcartlistbean.clicknum = obj.getString( "clicknum" );
                        jifenGoodcartlistbean.inventory = obj.getString( "inventory" );
                        jifenGoodcartlistbean.sales_volume = obj.getString( "sales_volume" );
                        jifenGoodcartlistbean.virtual_volume = obj.getString( "virtual_volume" );
                        jifenGoodcartlistbean.sku_str = obj.getString( "sku_str" );
                        jifenGoodcartlistbean.sku_arr = obj.getString( "sku_arr" );
//                        jifenGoodcartlistbean.postage=obj.getString( "postage");
                        jifenGoodcartlistbean.goods_sku = obj.getString( "goods_sku" );

                        lists.add( jifenGoodcartlistbean );
                    }

                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            jifengoodCartAdapter = new MallGoodsCartAdapter( MallGoodsCartActivity.this,lists );
                            listView.setAdapter( jifengoodCartAdapter );
                            jifengoodCartAdapter.setsubClickListener( new MallGoodsCartAdapter.SubClickListener() {
                                @Override
                                public void OntopicClickListener(View v, String detail, boolean bool, int posit) {
                                    if (detail.equals( "del" )){//删除
                                        goodid = lists.get( posit ).goods_id;
                                        goodsku = lists.get( posit ).goods_sku;
                                        delgooditem(posit);
                                    }else if (detail.equals( "jia" )){//加数量
                                        int goodnum = Integer.parseInt( lists.get( posit ).goods_num ) ;
                                        goodnum++;
                                        lists.get( posit ).goods_num = goodnum+"";
                                        imputedprice();
                                        jifengoodCartAdapter.notifyDataSetChanged();
                                    }else if (detail.equals( "jian" )){//减数量
                                        int goodnum = Integer.parseInt( lists.get( posit ).goods_num );
                                        if (goodnum == 1){

                                        }else {
                                            goodnum = Integer.parseInt( lists.get( posit ).goods_num );
                                            goodnum--;
                                            lists.get( posit ).goods_num = goodnum + "";
                                            imputedprice();
                                            jifengoodCartAdapter.notifyDataSetChanged();
                                        }
                                    }else if (detail.equals( "ischeck" )){//单个是否选中
                                        isitem();

                                    }
                                }
                            } );
                        }
                    } );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //全选按钮
    public void isall(){
        if (allischeck){
            for (int i = 0;i<lists.size();i++){
                lists.get( i ).ischeck = false;
            }
            imputedprice();
            jifengoodCartAdapter.notifyDataSetChanged();
        }else{
            for (int i = 0;i<lists.size();i++){
                lists.get( i ).ischeck = true;
            }
            imputedprice();
            jifengoodCartAdapter.notifyDataSetChanged();
        }
    }
    //单个是否选中
    public void isitem(){
        int ss = 0;
        for (int i = 0;i<lists.size();i++){
            if (lists.get(i).ischeck){
                ss++;
                if (ss == lists.size()){
                    allischeck = true;
                    iv_allcheck.setImageResource(R.drawable.unsel_check);
                }
            }else{
                allischeck = false;
                iv_allcheck.setImageResource(R.drawable.sel_check);

            }
        }
        imputedprice();
    }
    //计算价格
    public void imputedprice(){
        float allprics = 0;
        int isss = 0;
        for (int i = 0;i<lists.size();i++){
            if (lists.get(i).ischeck){
                allprics = allprics+(Float.valueOf(lists.get(i).price)*Float.valueOf(lists.get(i).goods_num));

                tv_alljifen.setText(new BigDecimal(allprics).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()+"元");
            }
            if (!lists.get(i).ischeck){
                isss++;
                if (isss == lists.size()){
                    tv_alljifen.setText("0");
                }
            }
        }
    }
    //删除购物车商品
    public void delgooditem(final int position){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body;
        if("".equals(goodsku)||goodsku==null||"null".equals(goodsku)){
            body = new FormBody.Builder().add("token", token).add( "goods_id",goodid ).build();
        }else{
            body = new FormBody.Builder().add("token", token).add( "goods_id",goodid ).add( "goods_sku",goodsku ).build();
        }
        Request request = new Request.Builder().url(Constants.APP_IP +"/api/Shopcart/del").post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    final String msg = jsonObject.getString("msg");
                    if ("0".equals(code)) {
                        // JSONObject jsonData = jsonObject.getJSONObject("data");
                        // today_order_num = jsonData.getString("AppParameters");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lists.remove(position);
                                jifengoodCartAdapter.notifyDataSetChanged();
                                imputedprice();
                                T.showShort(MallGoodsCartActivity.this,msg);
                            }
                        });

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                T.showShort(MallGoodsCartActivity.this,msg);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
