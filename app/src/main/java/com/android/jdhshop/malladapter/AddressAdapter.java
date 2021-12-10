package com.android.jdhshop.malladapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.jdhshop.R;
import com.android.jdhshop.mall.EditAddressActivity;
import com.android.jdhshop.mallbean.AddressBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class AddressAdapter extends CommonAdapter<AddressBean> {
    public AddressAdapter(Context context, int layoutId, List<AddressBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final AddressBean addressBean, int position) {
        holder.setText(R.id.txt_name,addressBean.consignee);
        holder.setText(R.id.txt_phone,addressBean.contact_number);
        holder.setText(R.id.txt_address,addressBean.province+addressBean.city+addressBean.county+addressBean.detail_address);
        holder.getView(R.id.txt_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("address",addressBean);
                Intent intent=new Intent(mContext, EditAddressActivity.class);
                intent.putExtra("address",bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
