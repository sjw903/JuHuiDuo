package com.android.jdhshop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.jdhshop.R;
import com.android.jdhshop.bean.KeyValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by lifuzhen on 17/8/21.
 */

public class KeyValueAdapter extends BaseAdapter {

    List<KeyValueBean> dataList;
    private Context mContext;
    private int type=0;
    private final LayoutInflater inflater;// 将布局文件转化为java代码

    //上个页面传过来的keySelected
    private String keySelected="";

    //是否显示 选中图标
    private Boolean isShowSelectedIcon = true;
    public void setShowSelectedIcon(Boolean showSelectedIcon) {
        isShowSelectedIcon = showSelectedIcon;
    }

    public KeyValueAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }
    public KeyValueAdapter(Context mContext, int type) {
        this.mContext = mContext;
        this.type = type;
        inflater = LayoutInflater.from(mContext);
    }

    //绑定数据
    public void setData(List<KeyValueBean> dataList){
        if (dataList == null){
            this.dataList = new ArrayList<>();
        }else{
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }

    //获取选中的ID
    public String setKey(String keySelected){
       this.keySelected = keySelected;
       return keySelected;
    }

    @Override
    public int getCount() {
        return dataList==null?0:dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_keyvalue, null);
            holder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
            holder.tv_select = (TextView) convertView.findViewById(R.id.tv_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        KeyValueBean object = dataList.get(position);
        if (object instanceof KeyValueBean){
            KeyValueBean keyValueBean = (KeyValueBean) object;
            if(1==type){
                holder.tv_value.setText(keyValueBean.name);
            }else{
                holder.tv_value.setText(keyValueBean.value);
            }

//            if (isShowSelectedIcon){
//                holder.tv_select.setVisibility(View.GONE);
//                holder.tv_value.setGravity(Gravity.CENTER_VERTICAL);
//            }else{
//                holder.tv_select.setVisibility(View.GONE);
//                holder.tv_value.setGravity(Gravity.CENTER);
//            }

            if(!TextUtils.isEmpty(keySelected)){
                    if (keySelected.equalsIgnoreCase(keyValueBean.key)){//ID与列表中ID相同则选中
                        holder.tv_value.setTextColor(mContext.getResources().getColor(R.color.app_main_color));
                        if(isShowSelectedIcon){
                            holder.tv_select.setVisibility(View.VISIBLE);
                        }else{
                            holder.tv_select.setVisibility(View.GONE);
                        }
                    }else{
                        holder.tv_value.setTextColor(mContext.getResources().getColor(R.color.col_title));
                        holder.tv_select.setVisibility(View.GONE);
                    }
            }else{
                holder.tv_value.setTextColor(mContext.getResources().getColor(R.color.col_title));
                holder.tv_select.setVisibility(View.GONE);
            }
        }


        //字体居中
        if(isShowSelectedIcon){
            holder.tv_value.setGravity(Gravity.CENTER_VERTICAL);
        }else{
            holder.tv_value.setGravity(Gravity.CENTER);
        }

        return convertView;
    }

    //定义内部缓存类
    class ViewHolder{
        TextView tv_value;
        TextView tv_select;
    }

}
