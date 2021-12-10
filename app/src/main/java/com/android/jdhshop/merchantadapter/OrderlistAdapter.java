package com.android.jdhshop.merchantadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.merchantactivity.CommentActivity;
import com.android.jdhshop.merchantactivity.OrderlistmsgActivity;
import com.android.jdhshop.merchantbean.Orderlistbean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 订单列表适配器
 */

public class OrderlistAdapter extends BaseAdapter {

    private Context context;
    private List<Orderlistbean> lists;
    private SubClickListener subClickListener;

    public OrderlistAdapter(Context context, List<Orderlistbean> list) {
        this.context = context;
        this.lists = list;
    }

    public void setsubClickListener(SubClickListener topicClickListener) {
        this.subClickListener = topicClickListener;
    }

    public interface SubClickListener {
        void OntopicClickListener(View v, String detail, int posit);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_orderlist, null);
            viewHold = new ViewHold();
            viewHold.tv_ddbh = convertView.findViewById(R.id.itemorderlist_ddbh);
            viewHold.tv_ddtype = convertView.findViewById(R.id.itemorderlist_ddtype);
            viewHold.tv_name = convertView.findViewById(R.id.itemorderlist_tvname);
            viewHold.tv_pric = convertView.findViewById(R.id.itemorderlist_tvpric);
            viewHold.tv_oldpric = convertView.findViewById(R.id.itemorderlist_tvoldpric);
            viewHold.tv_sku = convertView.findViewById(R.id.itemorderlist_tvsku);
            viewHold.tv_num = convertView.findViewById(R.id.itemorderlist_tvnum);
            viewHold.iv_img = convertView.findViewById(R.id.itemorderlist_ivimg);
            viewHold.tv_btone = convertView.findViewById(R.id.itemorderlist_tvbtone);
            viewHold.tv_bttwo = convertView.findViewById(R.id.itemorderlist_tvbttwo);
            viewHold.tv_btthree = convertView.findViewById(R.id.itemorderlist_tvbtthree);
            viewHold.ly_gomsg = convertView.findViewById(R.id.itemorderlist_gomsg);

            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tv_ddbh.setText("订单编号:" + lists.get(position).order_detail_id);
        viewHold.tv_name.setText(lists.get(position).goods_name);
        viewHold.tv_pric.setText("¥" + lists.get(position).price);
        if (lists.get(position).sku != null && !lists.get(position).sku.equals("") && !lists.get(position).sku.equals("null")) {
            String str = lists.get(position).sku.replace("&nbsp", "");
            str = str.replace(";;", " ");
            viewHold.tv_sku.setText(str);
        } else {
            viewHold.tv_sku.setText("");
        }
        viewHold.tv_num.setText("x" + lists.get(position).num);
        Glide.with(context).load(Constants.APP_IP + lists.get(position).img).into(viewHold.iv_img);
        if (lists.get(position).status.equals("1")) {//待付款
            viewHold.tv_ddtype.setText("");
            viewHold.tv_btthree.setVisibility(View.GONE);
            viewHold.tv_btone.setVisibility(View.GONE);
            viewHold.tv_bttwo.setTextColor(Color.parseColor("#F14B11"));
            viewHold.tv_bttwo.setBackgroundResource(R.drawable.orderlist_btss);
            viewHold.tv_bttwo.setText("付款");
            viewHold.tv_bttwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (subClickListener != null) {
                        subClickListener.OntopicClickListener(view, "fk", position);
                    }
                }
            });
            viewHold.ly_gomsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderlistmsgActivity.class);
                    intent.putExtra("orderid", lists.get(position).order_id);
                    intent.putExtra("orderdetailid", lists.get(position).order_detail_id);
                    intent.putExtra("title", "待付款");
                    context.startActivity(intent);
                }
            });
        } else if (lists.get(position).status.equals("2")) {//已付款，待发货
            viewHold.tv_ddtype.setText("买家已付款");
            viewHold.tv_btthree.setVisibility(View.GONE);
            viewHold.tv_bttwo.setVisibility(View.VISIBLE);
//            viewHold.tv_btone.setTextColor( Color.parseColor( "#989898" ) );
//            viewHold.tv_btone.setBackgroundResource( R.drawable.orderlist_bt );
//            viewHold.tv_btone.setText( "提醒发货" );
            viewHold.tv_btone.setTextColor(Color.parseColor("#989898"));
            viewHold.tv_btone.setBackgroundResource(R.drawable.orderlist_bt);
            viewHold.tv_btone.setText("取消订单");
            viewHold.tv_btone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (subClickListener != null) {
                        subClickListener.OntopicClickListener(view, "qxdd", position);
                    }
                }
            });
            viewHold.ly_gomsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderlistmsgActivity.class);
                    intent.putExtra("orderid", lists.get(position).order_id);
                    intent.putExtra("orderdetailid", lists.get(position).order_detail_id);
                    intent.putExtra("title", "待发货");
                    context.startActivity(intent);
                }
            });
        } else if (lists.get(position).status.equals("3")) {//已发货，待确认收货
            viewHold.tv_ddtype.setText("卖家已发货");
            viewHold.tv_btthree.setVisibility(View.GONE);
            viewHold.tv_btone.setVisibility(View.GONE);
            viewHold.tv_btone.setTextColor(Color.parseColor("#989898"));
            viewHold.tv_btone.setBackgroundResource(R.drawable.orderlist_bt);
            viewHold.tv_btone.setText("查看物流");
            viewHold.tv_bttwo.setTextColor(Color.parseColor("#989898"));
            viewHold.tv_bttwo.setBackgroundResource(R.drawable.orderlist_bt);
            viewHold.tv_bttwo.setText("确认收货");
            viewHold.ly_gomsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderlistmsgActivity.class);
                    intent.putExtra("orderid", lists.get(position).order_id);
                    intent.putExtra("orderdetailid", lists.get(position).order_detail_id);
                    intent.putExtra("title", "已发货");
                    context.startActivity(intent);
                }
            });
        } else if (lists.get(position).status.equals("4")) {//已完成
            viewHold.tv_ddtype.setText("交易成功");
            viewHold.tv_btone.setVisibility(View.GONE);
            viewHold.tv_btthree.setVisibility(View.GONE);
            viewHold.tv_btone.setTextColor(Color.parseColor("#989898"));
            viewHold.tv_btone.setBackgroundResource(R.drawable.orderlist_bt);
            viewHold.tv_btone.setText("申请退款");
            viewHold.tv_bttwo.setTextColor(Color.parseColor("#989898"));
            viewHold.tv_bttwo.setBackgroundResource(R.drawable.orderlist_bt);
            viewHold.tv_bttwo.setText("查看物流");
            viewHold.tv_btthree.setBackgroundResource(R.drawable.orderlist_btss);
            viewHold.tv_btthree.setTextColor(Color.parseColor("#F14B11"));
            viewHold.tv_btthree.setText("评价");
            viewHold.tv_btone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (subClickListener != null) {
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("order_id", lists.get(position).order_id);
                        intent.putExtra("goods_id",lists.get(position).goods_id);
                        context.startActivity(intent);
                    }
                }
            });
            viewHold.tv_bttwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (subClickListener != null) {
                        subClickListener.OntopicClickListener(view, "ckwl", position);
                    }
                }
            });
            viewHold.tv_btthree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (subClickListener != null) {
                        subClickListener.OntopicClickListener(view, "pj", position);
                    }
                }
            });
            viewHold.ly_gomsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderlistmsgActivity.class);
                    intent.putExtra("orderid", lists.get(position).order_id);
                    intent.putExtra("orderdetailid", lists.get(position).order_detail_id);
                    intent.putExtra("title", "已完成");
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }


    private static class ViewHold {
        private TextView tv_ddbh, tv_ddtype, tv_name, tv_pric, tv_oldpric, tv_sku, tv_num, tv_btone, tv_bttwo, tv_btthree;
        private ImageView iv_img;
        private LinearLayout ly_gomsg;
    }

    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }
}
