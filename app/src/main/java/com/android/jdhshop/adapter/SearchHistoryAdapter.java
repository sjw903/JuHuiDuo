package com.android.jdhshop.adapter;

import android.content.Context;
import android.view.View;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.SearchHistoryBean;
import com.android.jdhshop.common.ACache;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.utils.BroadcastContants;
import com.android.jdhshop.utils.BroadcastManager;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.Serializable;
import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/28 13:31
 * 说明：搜索历史适配器
 */
public class SearchHistoryAdapter extends CommonAdapter<SearchHistoryBean> {
   private int position;
    private ACache aCache;
    public SearchHistoryAdapter(Context context, int layoutId, List<SearchHistoryBean> datas) {
        super(context, layoutId, datas);
        aCache = ACache.get(context);
    }

    @Override
    protected void convert(ViewHolder viewHolder, SearchHistoryBean item, final int position) {
        //内容
        viewHolder.setText(R.id.search_content, item.getContent());
        viewHolder.setOnClickListener( R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SearchHistoryBean> searchHistoryBeans = (List<SearchHistoryBean>) aCache.getAsObject(Constants.HISTORICAL_RECORDS);
              if(null!=searchHistoryBeans&&searchHistoryBeans.size()>0){
                  searchHistoryBeans.remove( position);
                  aCache.put( Constants.HISTORICAL_RECORDS, (Serializable) searchHistoryBeans);
                  notifyDataSetChanged();
//                  BroadcastManager.getInstance(mContext).sendBroadcast( BroadcastContants.sendRefreshHistoryList,1);
              }
            }
        } );
    }
}
