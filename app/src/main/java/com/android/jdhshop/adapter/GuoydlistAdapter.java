package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.PromotionDetailsActivity;
import com.android.jdhshop.bean.Gydlistbean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;


public class GuoydlistAdapter extends RecyclerView.Adapter<GuoydlistAdapter.MyHolder> {

    private Context context;
    private List<Gydlistbean> lists;
    private SubClickListener subClickListener;
    DecimalFormat df=new DecimalFormat("0.00");
    public GuoydlistAdapter(Context context, List<Gydlistbean> list) {
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
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_gydlist , parent , false);
        MyHolder myHolder = new MyHolder( view );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.tvtitle.setText( lists.get( position ).title );
        holder.tv_quan.setText( "领券立减"+lists.get( position ).coupon_info_money+"元" );
        holder.tv_xiaoliang.setText( lists.get( position ).volume+"已抢" );
        holder.tv_newpric.setText( "¥"+lists.get( position ).quanhou_jiage );
        holder.tv_oldpric.setText( "¥"+lists.get( position ).size );
        Glide.with( context ).load( lists.get( position ).pict_url ).into( holder.iv_img );
        if (lists.get( position ).user_type.equals( "0" )){//淘宝
            Glide.with( context ).load( R.mipmap.home_taobao ).into(  holder.iv_smimg);

        }else if (lists.get( position ).user_type.equals( "1" )){//天猫
            Glide.with( context ).load( R.mipmap.home_tmall ).into(  holder.iv_smimg);
        }else{
            holder.iv_smimg.setVisibility( View.GONE );
        }

        Calendar nowDate= Calendar.getInstance();
//        Calendar oldDate=Calendar.getInstance();
        nowDate.setTime(new Date());//设置为当前系统时间
//        oldDate.setTime(timeStamp2Date( haobanlist.get( 0 ).end_time, "yyyy-MM-dd HH:mm:ss"));//设置为想要比较的日期
        Long timeNow=nowDate.getTimeInMillis();
//        Long timeOld=oldDate.getTimeInMillis();
        Long time = 1607788800000l-Long.valueOf( getTimeStame() );//相差毫秒数
        Log.e( "time",getTimeStame() );
        holder.countdownView.start( time );

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("num_iid",lists.get(position).tao_id);
                bundle.putString("price", lists.get(position).quanhou_jiage);
                Intent intent = new Intent( context,PromotionDetailsActivity.class );
                intent.putExtras( bundle );
                context.startActivity( intent );
//               context.openActivity(PromotionDetailsActivity.class, bundle);
//                if (subClickListener!=null){
//                    subClickListener.OntopicClickListener( view,"jy",position );
//                }
            }
        } );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    /**
     * 获取当前的时间戳
     * @return
     */
    public String getTimeStame() {
        //获取当前的毫秒值
        long time = System.currentTimeMillis();
        //将毫秒值转换为String类型数据
        String time_stamp = String.valueOf(time);
        //返回出去
        return time_stamp;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    //初始化控件
    class MyHolder extends RecyclerView.ViewHolder{

        private TextView tvtitle,tv_button,tv_newpric,tv_oldpric,tv_quan,tv_xiaoliang;
        private CountdownView countdownView;
        private ImageView iv_img,iv_smimg;
        public MyHolder(View itemView) {
            super( itemView );
            tvtitle = itemView.findViewById( R.id.itemgyd_title );
            tv_button = itemView.findViewById( R.id.itemgyd_button );
            tv_newpric = itemView.findViewById( R.id.itemgyd_newpric );
            tv_oldpric = itemView.findViewById( R.id.itemgyd_oldpric );
            tv_quan = itemView.findViewById( R.id.itemgyd_quan );
            tv_xiaoliang = itemView.findViewById( R.id.itemgyd_xiaoliang );
            iv_img = itemView.findViewById( R.id.itemgyd_img );
            iv_smimg = itemView.findViewById( R.id.itemgyd_smimg );
            countdownView = itemView.findViewById( R.id.itemgyd_countdownview );
        }
    }

    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }
    public static int getTimesnight(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis()/1000);
    }
}
