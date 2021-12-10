package com.android.jdhshop.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.WphDetailsActivity;
import com.android.jdhshop.bean.WphOrderBean;
import com.android.jdhshop.bean.Wphbean;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.widget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/06/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class WphOrderNewAdapter extends BaseAdapter {
    private Context context;
    private List<WphOrderBean> dataList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat( "0.00" );
    private LoadingDialog loadingDialog;

    public WphOrderNewAdapter(Context context) {
        this.context = context;
        loadingDialog = LoadingDialog.createDialog( context );
        loadingDialog.setMessage( "查询中..." );
        loadingDialog.setCanceledOnTouchOutside( false );
    }

    public void setData(List<WphOrderBean> dataList) {
        if (dataList == null) {
            this.dataList = new ArrayList<>();
        } else {
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from( context ).inflate( R.layout.item_order_new, null );
            holder.tv_name = convertView.findViewById( R.id.tv_name );
            holder.tv_stu = convertView.findViewById( R.id.tv_stu );
            holder.title_child = convertView.findViewById( R.id.title_child );
            holder.tv_date = convertView.findViewById( R.id.tv_date );
            holder.tv_one = convertView.findViewById( R.id.tv_one );
            holder.tv_two = convertView.findViewById( R.id.tv_two );
            holder.txt_copy1 = (TextView) convertView.findViewById( R.id.txt_copy1 );
            holder.txt_code = convertView.findViewById( R.id.order_num );
            holder.tv_three = convertView.findViewById( R.id.tv_three );
            holder.shop_image = convertView.findViewById( R.id.shop_image );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final WphOrderBean model = dataList.get( position );

        holder.tv_name.setText(model.getCommname() );
        holder.tv_stu.setText( model.ordersubstatusname );
        holder.txt_code.setText( "订单号:" + model.ordersn );
        holder.title_child.setText( model.getGoodsname() );
//        holder.tv_date.setText( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date( Long.valueOf( model.getOrder_time() ) ) ) );

        if (model.getCommission() == null) {
            holder.tv_one.setText( "0.00" );
        } else {
            holder.tv_one.setText( model.getCommission() + "" );
        }

        if (model.getCommissiontotalcost() != null)
            holder.tv_two.setText( model.getCommissiontotalcost() );
        if (model.getGoodsthumb() != null && model.getGoodsthumb().startsWith( "http" )) {
            Glide.with( context ).load( model.getGoodsthumb() ).dontAnimate().into( holder.shop_image );
        } else {
            Glide.with( context ).load( Constants.APP_IP + model.getGoodsthumb() ).placeholder( R.drawable.no_banner ).dontAnimate().into( holder.shop_image );
        }
        holder.txt_copy1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) context.getSystemService( Context.CLIPBOARD_SERVICE );
                cmb.setText( model.getOrdersn() );
                T.showShort( context, "订单号已复制" );
            }
        } );
        convertView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWphDetail( dataList.get( position ).getGoodsid() );
            }
        } );
        return convertView;
    }

    // 声明缓存类
    static class ViewHolder {
        TextView tv_name, tv_date, title_child, tv_stu, tv_one, tv_two, tv_three, txt_code, txt_copy1;//值
        ImageView shop_image;//
    }

    /**
     * @属性:获取唯品会商品详情
     * @开发者:wmm
     * @时间:2018/11/22 9:00
     */
    private void getWphDetail(final String goodsId) {
        RequestParams requestParams = new RequestParams();
        requestParams.put( "goodsId", goodsId );
        HttpUtils.post( Constants.APP_IP + "/api/WPH/goodsInfo",context, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (responseString != null) {
                    T.showShort( context, responseString );
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                loadingDialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                loadingDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    if (new JSONObject( responseString ).getInt( "code" ) != 0) {
                        T.showShort( context, "暂未查到该商品，或已下架" );
                        return;
                    }
                    JSONObject object = new JSONObject( responseString ).getJSONArray( "data" ).getJSONObject(0);
                    Intent intent = new Intent( context, WphDetailsActivity.class);
                    Gson gson = new Gson();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable( "goods", gson.fromJson( object.toString(),  Wphbean.class ) );
                    intent.putExtra( "goods", bundle );
                    context.startActivity( intent );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
    }
}
