package com.android.jdhshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.SearchHistoryBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.config.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yohn on 2018/8/25.
 */

public class SearchHistoryNewAdapter extends BaseAdapter {
    List<SearchHistoryBean> dataList;
    private Context mContext;
    private final LayoutInflater inflater;// 将布局文件转化为java代码
    private ACache aCache;
    public SearchHistoryNewAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from( mContext );
        aCache = ACache.get(mContext);
    }

    //绑定数据
    public void setData(List<SearchHistoryBean> dataList) {
        if (dataList == null) {
            this.dataList = new ArrayList<>();
        } else {
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate( R.layout.search_history_item, null );
            holder.tv_value = (TextView) convertView.findViewById( R.id.search_content );
            holder.tv_delete = (TextView) convertView.findViewById( R.id.tv_delete );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SearchHistoryBean object = dataList.get( position );
        holder.tv_value.setText( object.getContent() );
        holder.tv_delete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // List<SearchHistoryBean> searchHistoryBeans = (List<SearchHistoryBean>) aCache.getAsObject( Constants.HISTORICAL_RECORDS);
                if(null!=dataList&&dataList.size()>0){
                    dataList.remove( position);
                    aCache.put( Constants.HISTORICAL_RECORDS, (Serializable) dataList);
                    notifyDataSetChanged();
//                    BroadcastManager.getInstance(mContext).sendBroadcast( BroadcastContants.sendRefreshHistoryList,1);
                }
            }
        } );

            return convertView;
        }

    //定义内部缓存类
    class ViewHolder {
        TextView tv_value;
        TextView tv_delete;
    }
}
