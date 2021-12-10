package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;

import com.android.jdhshop.activity.JdDetailsActivity;
import com.android.jdhshop.activity.JinfenActivity;
import com.android.jdhshop.activity.PddDetailsActivity;
import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.bean.DaiTuiAiBean;
import com.android.jdhshop.bean.MyGoodsResp;
import com.android.jdhshop.bean.PDDBean;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.utils.OkHttpUtils;
import com.android.jdhshop.widget.LoadingDialog;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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


public class DaiTuiAiAdapter extends CommonAdapter<DaiTuiAiBean> {
    private ShopRecyclerAdapter.OnDeleteClickLister mDeleteClickListener;
    public OnItemClickListener itemClickListener;
    Gson gson=new Gson();
    private   View.OnClickListener viewonClickListener;

    public DaiTuiAiAdapter(Context context, int layoutId, List<DaiTuiAiBean> datas, View.OnClickListener onClickListener) {

        super(context, layoutId, datas);
        viewonClickListener=onClickListener;
    }

    @Override
    protected void convert(ViewHolder holder, DaiTuiAiBean item, int position) {
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
        ImageView shan = holder.getView(R.id.ai_daituiitem_delete);
        ImageView topjian = holder.getView(R.id.ai_daituiitem_topjiantou);
        BaseLogDZiYuan.LogDingZiYuan(topjian, "topjiantou.png");
        BaseLogDZiYuan.LogDingZiYuan(shan, "shanchuc.png");

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
                viewonClickListener.onClick(v);
                Toast.makeText(mContext, "置顶成功", Toast.LENGTH_SHORT).show();

            }
        });
        //holder.getView(R.id.ai_daituiitem_topjiantou).setOnClickListener(viewonClickListener);
        TextView shop_image = holder.getView(R.id.shop_image);
        Log.d("DaiTuiAiAdaper", "convert: " + item.platform_id);
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
                if (item.platform_id.equals("0")) {


                } else if (item.platform_id.equals("1")) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("apikey",Constants.JD_APP_KEY_NEW);
                    requestParams.put("isunion","1");
                    requestParams.put("keyword", item.product_id+"");
                    Map<String,Object> temp=new HashMap<>();
                    temp.put("apikey",Constants.JD_APP_KEY_NEW);
                    temp.put("isunion","1");
                    temp.put("keyword",item.product_id+"");
                    HttpUtils.post1(pddSearch+"",mContext, requestParams, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(responseString);

                                PDDBean newPddBeans ;

                                newPddBeans=gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("goods_list").getJSONObject(0).toString(),PDDBean.class);

                                Intent intent=new Intent(mContext, PddDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("goods", newPddBeans);
                                intent.putExtra("goods",bundle);

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



    public void caozuo(String id, String handle, String userid) {
        RequestParams params = new RequestParams();

        params.put("talk_group_product_id", id);
        params.put("handle", handle);
        params.put("talk_group_product_user_id", userid);
        HttpUtils.post(Constants.getTuiPinCaoZuo, mContext,params, new TextHttpResponseHandler() {
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
