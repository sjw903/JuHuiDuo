package com.android.jdhshop.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.MTBean;
import com.android.jdhshop.common.T;
import com.android.jdhshop.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/06/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MTAdapter extends  RecyclerView.Adapter<MTAdapter.ViewHolder> {

    private List<MTBean> mtBeanList;
    private LoadingDialog loadingDialog;
    private List<MTBean> dataList = new ArrayList<>();

    public MTAdapter(Context context) {
        loadingDialog = LoadingDialog.createDialog( context );
        loadingDialog.setMessage( "查询中..." );
        loadingDialog.setCanceledOnTouchOutside( false );
    }
    public void setData(List<MTBean> dataList) {
        if (dataList == null) {
            this.dataList = new ArrayList<>();
        } else {
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }
    public MTAdapter(List<MTBean> mtBeanList) {
        this.mtBeanList = mtBeanList;
    }

    @Override
    public MTAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_meituan, parent, false );
        MTAdapter.ViewHolder holder = new MTAdapter.ViewHolder( view );
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MTBean mtBean = mtBeanList.get( position );
        holder.shop_Name.setText( mtBean.shopname );
        holder.order_Pay_Time.setText( mtBean.orderpaytime );
        holder.balance_Amount.setText( mtBean.balanceamount );
        holder.actual_Order_Amount.setText( mtBean.actualorderamount );
        holder.order_num.setText( mtBean.order_num );


        holder.fuzhi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager) CaiNiaoApplication.getAppContext().getSystemService( Context.CLIPBOARD_SERVICE );
                cmb.setText(mtBean.getOrder_num());
                T.showShort(CaiNiaoApplication.getAppContext(),"订单号已复制");
            }
        } );
    }
    @Override
    public int getItemCount() {
        return mtBeanList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shop_Name;
        TextView order_Pay_Time;
        TextView it_text;
        TextView balance_Amount;
        TextView it_text_1;
        TextView actual_Order_Amount;
        TextView it_text_2;
        TextView order_num;
        TextView fuzhi;
        public ViewHolder(View itemView) {
            super(itemView);
            shop_Name = (TextView) itemView.findViewById( R.id.shop_Name );
            order_Pay_Time = (TextView) itemView.findViewById( R.id.order_Pay_Time );
            it_text = (TextView) itemView.findViewById( R.id.it_text );
            balance_Amount = (TextView) itemView.findViewById( R.id.balance_Amount );
            it_text_1 = (TextView) itemView.findViewById( R.id.it_text_1 );
            actual_Order_Amount = (TextView) itemView.findViewById( R.id.actual_Order_Amount );
            it_text_2 = (TextView) itemView.findViewById( R.id.it_text_2 );
            order_num = (TextView) itemView.findViewById( R.id.order_num );
            fuzhi = (TextView) itemView.findViewById( R.id.fuzhi );

        }
    }
}







   
        
      






