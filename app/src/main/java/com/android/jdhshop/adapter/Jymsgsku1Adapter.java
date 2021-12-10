package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.Jymsgbean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 */

public class Jymsgsku1Adapter extends BaseAdapter {

    private Context context;
    private SubClickListener subClickListener;
    private List<Jymsgbean.Item.oilPriceList.gunNos> list;
    public int selectindex = 0;
    public Jymsgsku1Adapter(Context context, List<Jymsgbean.Item.oilPriceList.gunNos> list) {
        this.list = list;
        this.context = context;
    }
    public void setSelectindex(int selectindex){
        this.selectindex = selectindex;
    }
    public void setsubClickListener(SubClickListener topicClickListener) {
        this.subClickListener = topicClickListener;
    }

    public interface SubClickListener {
        void OntopicClickListener(View v, boolean bs, int detail, String posit);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int positiona, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_jymsgsku, null);
            viewHold = new ViewHold();
            viewHold.tv_type = convertView.findViewById( R.id.item_name );
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        if (selectindex == positiona){
            viewHold.tv_type.setBackgroundResource( R.drawable.commodity_listitem_ck );
            viewHold.tv_type.setTextColor( Color.parseColor( "#ffffff" ) );
        }else{
            viewHold.tv_type.setBackgroundResource( R.drawable.commodity_listitem );
            viewHold.tv_type.setTextColor( Color.parseColor( "#000000" ) );
        }

            viewHold.tv_type.setText( list.get( positiona ).gunNo+"号" );
        notifyDataSetChanged();
//        final String[] str = new String[lists.size()];
//        for (int i = 0;i<lists.size();i++){
//            str[i] = lists.get( positiona ).oilName;
//        }


        return convertView;
    }


    private static class ViewHold {
        private TextView tv_type;
    }
    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }
}
