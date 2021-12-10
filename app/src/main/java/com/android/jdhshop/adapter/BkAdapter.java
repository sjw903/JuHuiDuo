package com.android.jdhshop.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.activity.TaskBigImgActivity;
import com.android.jdhshop.bean.BkBean;
import com.android.jdhshop.bean.MessageEvent;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.utils.BitmapUtils;
import com.android.jdhshop.utils.RelativeDateFormat;
import com.android.jdhshop.utils.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.android.jdhshop.utils.UIUtils.getString;

public class BkAdapter extends CommonAdapter<BkBean.BKItem> {
    DecimalFormat df=new DecimalFormat("0.00");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
    public BkAdapter(Context context, int layoutId, List<BkBean.BKItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final BkBean.BKItem item, final int position) {
        //设置标题
        TextView title = viewHolder.getView(R.id.txt_content);
        String  ss=item.copy_content.replaceAll("&lt;br&gt;","<br/>");
        title.setText(Html.fromHtml(item.title+"<br/>"+"原价: "+item.itemprice+"<br/>"+"限时特惠: "+item.itemendprice+"<br/><br/>"+ss));
        Glide.with(mContext).load(R.mipmap.app_icon).dontAnimate().into((ImageView) viewHolder.getView(R.id.img_head));
        viewHolder.setText(R.id.txt_nickname, getString(R.string.app_name));
        Date date = null;
//        try {
////            date = format.parse(item.add_time);
            viewHolder.setText(R.id.txt_time, RelativeDateFormat.format(new Date(Long.valueOf(item.add_time)*1000)));
//        } catch (ParseException e) {
//            e.printStackTrace();
//            viewHolder.setText(R.id.txt_time,item.add_time);
//        }
        viewHolder.setText(R.id.txt_share_num, item.dummy_click_statistics);
        try {
            viewHolder.setText(R.id.txt_high_yj, "最高奖:¥" + (String.format("%.2f", (StringUtils.doStringToDouble(item.itemendprice))*((float) SPUtils.getIntData(mContext, "rate", 50) / 100)*Double.valueOf(item.tkrates)/100)).replace(".00", ""));
        } catch (NumberFormatException e) {
            viewHolder.setText(R.id.txt_high_yj, "最高奖:¥" +(String.format("%.2f", StringUtils.doStringToDouble(item.itemendprice)*((float) SPUtils.getIntData(mContext, "rate", 50) / 100)*Double.valueOf(item.tkrates)/100)).replace(".00", ""));
        }
        viewHolder.getView(R.id.txt_coupon_amount).setVisibility(View.GONE);
//        viewHolder.setText(R.id.txt_coupon_amount, "优惠券:¥" + item.coupon_amount+"      券后:¥"+df.format(Double.valueOf(item.zk_final_price==null?"0":item.zk_final_price)-Double.valueOf(item.coupon_amount==null?"0":item.coupon_amount)));
        viewHolder.setText(R.id.txt_desc,"【正确下单步骤：识别图中二维码→领券购买复制淘口令→点开手机淘宝领券下单！！】");
        GridView gridView = viewHolder.getView(R.id.item_grid);
        final List<String> list = new ArrayList<>();
        viewHolder.getView(R.id.txt_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "【正确下单步骤：识别图中二维码→领券购买复制淘口令→点开手机淘宝领券下单！！】");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtils.showShortToast(mContext, "复制成功");
            }
        });
        if (item.itempic!= null) {
            list.addAll(item.itempic);
        }else{
            list.add(item.sola_image);
        }
        gridView.setLayoutParams(new LinearLayout.LayoutParams(BitmapUtils.getScreenWith(mContext),ViewGroup.LayoutParams.WRAP_CONTENT));
        GridVIewAdapter adapter = new GridVIewAdapter(mContext, R.layout.shequ_item_grid, list.size()>9?list.subList(0,9):list,0);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> recordPaths = new ArrayList<>(); //缺陷记录的图片集合
                for(int i=0;i<list.size();i++){
                    recordPaths.add(list.get(i)+"_310x310.jpg");
                }
                Intent imgIntent = new Intent(mContext, TaskBigImgActivity.class);
                imgIntent.putStringArrayListExtra("paths",recordPaths);
                imgIntent.putExtra("title","图片");
                imgIntent.putExtra("position",position);
                mContext.startActivity(imgIntent);
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid", item.itemid);
                Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
//                RequestParams requestParams = new RequestParams();
//                requestParams.put("id", item.goods_id);
//                HttpUtils.post(Constants.UPDATE_SHARE_NUM, requestParams, new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    }
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
////                        getDatas().get(position).setShare_num((Integer.valueOf( getDatas().get(position).getShare_num())+1)+"");
////                        notifyDataSetChanged();
//                    }
//                });
            }
        });
        viewHolder.getView(R.id.txt_share_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageEvent messageEvent=new MessageEvent("share2");
                messageEvent.setPosition(position);
                EventBus.getDefault().post(messageEvent);
            }
        });
    }
}
