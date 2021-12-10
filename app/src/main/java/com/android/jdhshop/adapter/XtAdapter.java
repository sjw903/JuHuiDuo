package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.MainActivity;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.NewsActivity;
import com.android.jdhshop.activity.TaskBigImgActivity;
import com.android.jdhshop.activity.WebViewActivity;
import com.android.jdhshop.bean.MessageCenterBean;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.SuCaiBean;
import com.android.jdhshop.bean.XtBea;
import com.android.jdhshop.config.Constants;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class XtAdapter extends CommonAdapter<MessageCenterBean.MessageCenterChildBean> {

    public XtAdapter(Context context, int layoutId, List<MessageCenterBean.MessageCenterChildBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final MessageCenterBean.MessageCenterChildBean shopBean, final int position) {
        String img_url = shopBean.getImg().startsWith("http") ? shopBean.getImg() : Constants.APP_IP+shopBean.getImg();
//        img_url = "";
        Glide.with(mContext).load(img_url).error(R.drawable.xt).into((ImageView) holder.getView(R.id.round_img));
        holder.setText(R.id.txt_name,shopBean.getTitle());
        holder.setText(R.id.txt_address,shopBean.getPubtime().substring(0,shopBean.getPubtime().length()-3));
        holder.setText(R.id.txt_share_num,shopBean.getClicknum()+"人阅读");
        holder.setText(R.id.tx_desc,shopBean.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsActivity.class);
                intent.putExtra("title", shopBean.getTitle());
                intent.putExtra("article_id",shopBean.getArticle_id());
                intent.putExtra("type","1");
                mContext.startActivity(intent);
            }
        });
    }
}
