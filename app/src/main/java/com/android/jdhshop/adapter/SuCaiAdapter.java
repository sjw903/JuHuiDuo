package com.android.jdhshop.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.activity.TaskBigImgActivity;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.bean.SuCaiBean;
import com.android.jdhshop.bean.TaobaoGuestBean;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.StringUtils;
import com.android.jdhshop.utils.WxUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SuCaiAdapter extends CommonAdapter<SuCaiBean> {

    public SuCaiAdapter(Context context, int layoutId, List<SuCaiBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final SuCaiBean item, final int position) {
        //设置标题
        TextView title = viewHolder.getView(R.id.txt_content);
        title.setText(item.getMob_text());
        if("".equals(item.getMob_text())){
            title.setVisibility(View.GONE);
        }else{
            title.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext).load(item.getAvatar()).placeholder(R.mipmap.icon_defult_boy).dontAnimate().into((ImageView)viewHolder.getView(R.id.img_head));
        viewHolder.setText(R.id.txt_nickname,item.getNickname());
        viewHolder.setText(R.id.txt_time,item.getPubtime());
        viewHolder.setText(R.id.txt_share_num,item.getShare_num());
        viewHolder.getView(R.id.txt_high_yj).setVisibility(View.GONE);
        viewHolder.getView(R.id.txt_coupon_amount).setVisibility(View.GONE);
        GridView gridView=viewHolder.getView(R.id.item_grid);
        title = viewHolder.getView(R.id.txt_desc);
        if(CaiNiaoApplication.getUserInfoBean()!=null){
            title.setText(Html.fromHtml("苹果下载地址:"+SPUtils.getStringData(mContext,"down_ios","")+"<br/>"+"安卓下载地址:"+SPUtils.getStringData(mContext,"down_android_yyb","")+"<br/>"+"邀请码:"+CaiNiaoApplication.getUserInfoBean().user_msg.auth_code));
        }else{
            title.setText(Html.fromHtml("苹果下载地址:"+SPUtils.getStringData(mContext,"down_ios","")+"<br/>"+"安卓下载地址:"+SPUtils.getStringData(mContext,"down_android_yyb","")));
        }
        TextView finalTitle = title;
        viewHolder.getView(R.id.txt_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", finalTitle.getText().toString().trim());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtils.showShortToast(mContext, "复制成功");
            }
        });
        String[] datas=item.getMob_img().replace("[","").replace("]","").split(",");
        final List<String> list=new ArrayList();
        for(int i=0;i<datas.length;i++)
            list.add(datas[i]);
        GridVIewAdapter adapter = new GridVIewAdapter(mContext, R.layout.shequ_item_grid, list,1);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> recordPaths = new ArrayList<>(); //缺陷记录的图片集合
                for(int i=0;i<list.size();i++){
                    recordPaths.add(list.get(i));
                }
                Intent imgIntent = new Intent(mContext, TaskBigImgActivity.class);
                imgIntent.putStringArrayListExtra("paths",recordPaths);
                imgIntent.putExtra("title","图片");
                imgIntent.putExtra("position",position);
                mContext.startActivity(imgIntent);
            }
        });
        if("".equals(item.getMob_img()))
            gridView.setVisibility(View.GONE);
        else
            gridView.setVisibility(View.VISIBLE);
        viewHolder.getView(R.id.txt_share_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageEvent messageEvent=new MessageEvent("share");
                messageEvent.setPosition(position);
                EventBus.getDefault().post(messageEvent);
             }
        });
    }
}
