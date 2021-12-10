package com.android.jdhshop.snadapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.HaoDanBeankuaiqiang;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 */

public class HomeHotAdapter extends CommonAdapter<HaoDanBeankuaiqiang> {

    public HomeHotAdapter(Context context,int lyid, List<HaoDanBeankuaiqiang> list) {
        super(context,lyid,list);
    }
    @Override
    protected void convert(ViewHolder holder, HaoDanBeankuaiqiang haoDanBean, int position) {
//                holder.getView( R.id.itemgoodlist_img ).setBackgroundResource( R.mipmap.about_logohdpi );
        TextView tv_pric = holder.getView( R.id.itemgoodlist_pric );
        TextView tv_oldpric = holder.getView( R.id.itemgoodlist_oldpric );
        TextView tv_qiang = holder.getView( R.id.itemgoodlist_qiang );
        TextView tv_lisheng = holder.getView( R.id.itemgoodlist_lisheng );
        TextView tv_name = holder.getView( R.id.itemgoodlist_name );
        ImageView iv_img = holder.getView( R.id.itemgoodlist_img );
        Glide.with( mContext ).load( haoDanBean.img ).into( iv_img );
        if (haoDanBean.couponPrice!=null&&!haoDanBean.couponPrice.equals( "" )) {
            tv_pric.setText( "¥" + (Double.parseDouble( haoDanBean.commodityPrice ) - Double.parseDouble( haoDanBean.couponPrice )) );
        }else{
            tv_pric.setText("¥" +  haoDanBean.commodityPrice );
        }
        tv_oldpric.setText("¥" +  haoDanBean.snPrice );
//        tv_lisheng.setText( "立省"+haoDanBean.couponmoney+"元" );
        tv_name.setText( haoDanBean.commodityName );
        tv_oldpric.getPaint().setFlags( Paint.STRIKE_THRU_TEXT_FLAG); //中划线




    }

//
//
//    @Override
//    protected void convert(ViewHolder holder, HaoDanBean haoDanBean, int position) {
//
//    }
//
//    public interface SubClickListener {
//        void OntopicClickListener(View v, String detail, int posit);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public void onBindViewHolder(MyHolder holder, final int position) {
////        holder.tv_name.setText( lists.get( position ).goods_name );
////        holder.tv_pric.setText( "¥ "+ lists.get( position ).price  );
////        holder.tv_num.setText( lists.get( position ).sales_volume+"人购买" );
////        Glide.with( context ).load( Urls.url+ lists.get( position ).img ).into( holder.iv_img );
////        holder.itemView.setOnClickListener( new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                if (subClickListener!=null){
////                        subClickListener.OntopicClickListener( view,"item",position );
////                }
////            }
////        } );
//    }
//
//    @Override
//    public int getItemCount() {
//        return lists.size();
//    }
//
//    @Override
//    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MyHolder myHolder = null;
//            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_goodlist, parent, false );
//             myHolder = new MyHolder( view );
//        return myHolder;
//    }
//
//    public static String timeStamp2Date(long time, String format) {
//        if (format == null || format.isEmpty()) {
//            format = "yyyy-MM-dd HH:mm:ss";
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        return sdf.format(new Date(time));
//    }
//
//    //初始化控件
//    class MyHolder extends RecyclerView.ViewHolder{
//
//        private TextView tv_name,tv_pric,tv_num;
//        private ImageView iv_img;
//
//        public MyHolder(View itemView) {
//            super( itemView );
//
//                tv_name = itemView.findViewById( R.id.itemgoodlist_name );
//                tv_pric = itemView.findViewById( R.id.itemgoodlist_pric );
//                iv_img = itemView.findViewById( R.id.itemgoodlist_img );
//                tv_num = itemView.findViewById( R.id.itemgoodlist_num );
//        }
//    }
}
