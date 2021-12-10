package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.activity.JdDetailsActivity;
import com.android.jdhshop.activity.PddDetailsActivity;
import com.android.jdhshop.activity.PddXiaoKaPianActivity;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.activity.SetAssistantActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.DaiTuiAiBean;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.bean.XiTongDaiTuiAiBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.OkHttpUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;

import static com.android.jdhshop.config.Constants.pddSearch;

public class XiTongDaiTuiAiAdapter extends CommonAdapter<XiTongDaiTuiAiBean> {
    private ShopRecyclerAdapter.OnDeleteClickLister mDeleteClickListener;
    private View.OnClickListener viewonClickListener;
    Gson gson = new Gson();

    public XiTongDaiTuiAiAdapter(Context context, int layoutId, List<XiTongDaiTuiAiBean> datas, View.OnClickListener onClickListener) {
        super(context, layoutId, datas);
        viewonClickListener = onClickListener;
    }

    @Override
    protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, XiTongDaiTuiAiBean item, int position) {
        String qunid = SPUtils.getStringData(mContext, "id", "");
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

        ImageView shan = holder.getView(R.id.ai_daituiitem_delete);
        ImageView topjian = holder.getView(R.id.ai_daituiitem_topjiantou);
        BaseLogDZiYuan.LogDingZiYuan(topjian, "topjiantou.png");
        BaseLogDZiYuan.LogDingZiYuan(shan, "shanchuc.png");

        //设置删除线
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText("原价:¥" + (item.price));
        tx2.setText("¥" + item.cur_price);
        holder.getView(R.id.ai_daituiitem_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caozuo(item.id, "1", item.user_id, qunid);
                getDatas().remove(position);
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });

        holder.getView(R.id.ai_daituiitem_topjiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestParams params = new RequestParams();
                params.put("gid", qunid);
                params.put("pid", item.platform_id);
                params.put("id", item.id);
                params.put("title", item.product_title);
                params.put("desc", item.product_desc);
                params.put("img", item.thumb_image_url);
                params.put("price", item.price);
                params.put("org_price", item.org_price);
                params.put("after_price", item.cur_price);
                params.put("commission", item.commission);
                params.put("ts_time", item.ticket_start_time);
                params.put("te_time", item.ticket_end_time);
                params.put("linkurl", item.linkurl);
                params.put("descurl", item.descurl);
                params.put("discount", item.discount);
                params.put("shopname", item.shopname);
                params.put("wenan", item.wenan);
                HttpUtils.post(Constants.getAddShngPin, mContext, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseString);
                            String msg = jsonObject.getString("msg");
                            caozuo(item.id, "1", item.user_id, qunid);
                            getDatas().remove(position);
                            Toast.makeText(mContext, "置顶成功", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                viewonClickListener.onClick(v);
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
        holder.getView(R.id.linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.platform_id.equals("0")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("num_iid", item.id);
                    bundle.putString("price", item.price);
                    Intent intent = new Intent(mContext, PromotionDetailsActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                } else if (item.platform_id.equals("1")) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
                    requestParams.put("isunion", "1");
                    requestParams.put("keyword", item.product_id + "");
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("apikey", Constants.JD_APP_KEY_NEW);
                    temp.put("isunion", "1");
                    temp.put("keyword", item.product_id + "");
                    HttpUtils.post1(pddSearch + "", mContext, requestParams, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseString);

                                PDDBean newPddBeans;

                                newPddBeans = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("goods_list").getJSONObject(0).toString(), PDDBean.class);

                                Intent intent = new Intent(mContext, PddDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("goods", newPddBeans);
                                intent.putExtra("goods", bundle);

                                mContext.startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String msg = jsonObject.optString("message");
                        }
                    });

                } else if (item.platform_id.equals("2")) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("apikey", Constants.JD_APP_KEY_NEW);
                    requestParams.put("goods_ids", item.product_id);
                    requestParams.put("isunion", "1");
                    OkHttpUtils.getInstance().get(Constants.JDNEWGOODS_LIST + "?" + requestParams.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogUtils.d("dsfasdf", e.toString());
                        }

                        @Override
                        public void onResponse(Call call, okhttp3.Response response) throws IOException {
                            String s = response.body().string();
                            LogUtils.d("dsfasdf", s);
                            try {
                                JSONObject object1 = new JSONObject(s);
                                JSONArray array = object1.getJSONObject("data").getJSONArray("data");
                                if (array == null || array.length() == 0) {

                                    return;
                                }
                                for (int i = 0; i < 1; i++) {
                                    MyGoodsResp resp = new Gson().fromJson(array.getJSONObject(i).toString(), MyGoodsResp.class);

                                    Intent intent = new Intent(mContext, JdDetailsActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("goods", resp);
                                    intent.putExtra("goods", bundle);
                                    mContext.startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public void caozuo(String id, String handle, String userid, String qunid) {
        RequestParams params = new RequestParams();
        params.put("talk_group_id", qunid);
        params.put("talk_group_product_id", id);
        params.put("handle", handle);
        params.put("talk_group_product_user_id", userid);
        HttpUtils.post(Constants.getTuiPinCaoZuo, mContext, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    String msg = jsonObject.getString("msg");
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
