package com.android.jdhshop.adapter;

import android.content.Context;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.MessageCenterBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/28 10:21
 * 说明：消息中心
 */
public class MessageCenterAdapter extends CommonAdapter<MessageCenterBean.MessageCenterChildBean> {

    public MessageCenterAdapter(Context context, int layoutId, List<MessageCenterBean.MessageCenterChildBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, MessageCenterBean.MessageCenterChildBean item, int position) {
        //设置标题
        viewHolder.setText(R.id.title, item.getTitle());
        //设置内容
//        viewHolder.setText(R.id.content, item.getPubtime());
    }
}
