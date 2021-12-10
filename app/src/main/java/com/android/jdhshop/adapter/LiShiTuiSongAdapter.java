package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.bean.DaiTuiAiBean;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LiShiTuiSongAdapter extends CommonAdapter<DaiTuiAiBean> {
    private ShopRecyclerAdapter.OnDeleteClickLister mDeleteClickListener;

    public LiShiTuiSongAdapter(Context context, int layoutId, List<DaiTuiAiBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, DaiTuiAiBean item, int position) {
        //设置图片
        ImageView imageView = holder.getView(R.id.image);
        Glide.with(mContext).load(item.thumb_image_url).error(R.drawable.no_banner).dontAnimate().into(imageView);
        TextView title = holder.getView(R.id.title_child);
        title.setText(item.product_title);
        TextView comm = holder.getView(R.id.ai_daituiitem_comm);
        comm.setText("推广费" + item.referrer_rate_commission);
        TextView fanlicomm = holder.getView(R.id.ai_daituifanli_comm);
        fanlicomm.setText("返利费" + item.fee_user_commission);
        TextView tx2 = holder.getView(R.id.tx2);
        TextView tx2_2 = holder.getView(R.id.tx2_2);
        TextView quan = holder.getView(R.id.tx3);
        quan.setText(item.discount + "元");
        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("原价:¥" + (item.price));
        if (item.cur_price.equals("0")) {
            tx2.setText("¥" + item.org_price);
        } else {
            tx2.setText("¥" + item.cur_price);
        }

        holder.getView(R.id.ai_daituiitem_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caozuo(item.id, "1", item.user_id);
                getDatas().remove(position);
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        holder.getView(R.id.ai_daituiitem_topjiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caozuo(item.id, "0", item.user_id);
                notifyDataSetChanged();
                Toast.makeText(mContext, "置顶成功", Toast.LENGTH_SHORT).show();
                //getDatas().clear();

            }
        });
        TextView shop_image = holder.getView(R.id.shop_image);
        if (item.platform_id.equals("1")) {
            shop_image.setText("拼多多");
        } else if (item.platform_id.equals("2")) {
            shop_image.setText("京东");
        } else if (item.platform_id.equals("3")) {
            shop_image.setText("淘宝");
        }
        holder.getView(R.id.remo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.platform_id.equals("1")) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("num_iid", item.id);
//                    bundle.putString("price", item.price);
//                    Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);

                } else if (item.platform_id.equals("2")) {
//                    Intent intent=new Intent(mContext, PddDetailsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("goods", getDatas().get(position).user_id);
//                    intent.putExtra("goods",bundle);
//                    mContext.startActivity(intent);

                } else if (item.platform_id.equals("3")) {


                }
            }
        });
    }
    public void caozuo(String id, String handle, String userid) {
        RequestParams params = new RequestParams();
        params.put("talk_group_product_id", id);
        params.put("handle", handle);
        params.put("talk_group_product_user_id", userid);
        HttpUtils.post(Constants.getTuiPinCaoZuo,mContext, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.getString("msg");

                    //holder.getView(R.id.remo).setVisibility(View.GONE);
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setOnDeleteClickListener(ShopRecyclerAdapter.OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }


    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
