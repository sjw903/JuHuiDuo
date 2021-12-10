package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.activity.ShopActivity;
import com.android.jdhshop.bean.HaoDanBean;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.bean.TodayHighlightsBean;
import com.android.jdhshop.bean.TodayHighlightsBean2;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/26 13:34
 * 说明：今日精选适配器
 */
public class TodayHighlightsAdapter extends CommonAdapter<TodayHighlightsBean2> {

    public TodayHighlightsAdapter(Context context, int layoutId, List<TodayHighlightsBean2> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final TodayHighlightsBean2 item, int position) {
        //设置标题
//        TextView title = viewHolder.getView(R.id.title);
        //更多
//        TextView seeMore = viewHolder.getVlistView1iew(R.id.seeMore);
        //listview
        GridView listView = viewHolder.getView(R.id.listView1);
//        title.setText(item.getTitle());
//        if ("必推精选".contains(TextUtils.isEmpty(item.getTitle()) ? "" : item.getTitle())) {
//            title.setTextColor(mContext.getResources().getColor(R.color.green_2));
//            seeMore.setTextColor(mContext.getResources().getColor(R.color.green_2));
//        } else if ("今日上新".contains(TextUtils.isEmpty(item.getTitle()) ? "" : item.getTitle())) {
//            title.setTextColor(mContext.getResources().getColor(R.color.app_main_color));
//            seeMore.setTextColor(mContext.getResources().getColor(R.color.app_main_color));
//        }

//        if (TextUtils.isEmpty(item.getTitle())) {
//            viewHolder.getView(R.id.bitui_bar).setVisibility(View.GONE);
//        } else {
//            viewHolder.getView(R.id.bitui_bar).setVisibility(View.VISIBLE);
//        }

//        seeMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ShopActivity.class);
//                intent.putExtra("title", "商品");
//                intent.putExtra("index", 0);
//                mContext.startActivity(intent);
//            }
//        });

        TodayHighlightsChildAdapter2 adapter = new TodayHighlightsChildAdapter2(mContext, R.layout.today_highlights_child_item2, item.getList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               HaoDanBean taobaoGuesChildtBean = item.getList().get(i);
                if (taobaoGuesChildtBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", taobaoGuesChildtBean.itemid);
                    bundle.putSerializable("bean",taobaoGuesChildtBean);
                    Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
