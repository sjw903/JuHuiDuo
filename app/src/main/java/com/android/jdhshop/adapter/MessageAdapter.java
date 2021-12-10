package com.android.jdhshop.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.MessageCenterBean;
import com.android.jdhshop.bean.ShopActicleBean;

import java.util.List;

public class MessageAdapter extends BaseQuickAdapter<MessageCenterBean.MessageCenterChildBean,BaseViewHolder> {
    public MessageAdapter(int layoutResId, @Nullable List<MessageCenterBean.MessageCenterChildBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageCenterBean.MessageCenterChildBean item) {
        helper.setText(R.id.txt_title,item.getTitle());
        helper.setText(R.id.txt_date,item.getPubtime());
        helper.setText(R.id.txt_content,Html.fromHtml(item.getClicknum()+" 人已读"));
    }
}
