package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.activity.AiEstablishActivity;
import com.android.jdhshop.activity.AiOpeningaiActivity;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;

import java.util.ArrayList;
import java.util.List;

import static com.android.jdhshop.R.color.red;

/**
 * 我的群信息页面适配器
 */
public class WeiXinQunAdapter extends RecyclerView.Adapter<WeiXinQunAdapter.MyViewHolder>{
    private final Context context;
    List<WeiXinQunXinXIBean> data = new ArrayList<WeiXinQunXinXIBean>();
    /*
    自定义的构造方法 ，传入上下文对象context 和data数据
     */
    public WeiXinQunAdapter(Context context, List<WeiXinQunXinXIBean> data){
        this.context = context;
        this.data = data;
    }
    /*
    getview
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.weixinqun_item_recy,null);
        return new MyViewHolder(view);
    }
    /*
    绑定数据
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WeiXinQunXinXIBean bean = data.get(position);
        String status = bean.status;

        if(status.equals("5")){
            holder.ai_weixin_status.setText("等待助理");
            holder.ai_weixinqun_genghuan.setText("邀助理入群");
            //设置item的点击事件
            holder.ai_weixinqun_genghuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,AiOpeningaiActivity.class);
                    intent.putExtra("tmpid", bean.tmp_id);
                    intent.putExtra("name", bean.group_title);
                    intent.putExtra("wx_service_user", bean.wx_service_user);
                    intent.putExtra("status", "5");
                    context.startActivity(intent);
                }
            });
        }else if(status.equals("1")){
            holder.ai_weixin_status.setText("等待审核");
            holder.ai_weixinqun_genghuan.setText("等待审核");
            holder.ai_weixinqun_genghuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "正在审核中", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(status.equals("2")){
            holder.ai_weixin_status.setText("审核通过");
            holder.ai_weixinqun_genghuan.setText("申请助理");
            holder.ai_weixinqun_genghuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,AiOpeningaiActivity.class);
                    intent.putExtra("tmpid", bean.tmp_id);
                    intent.putExtra("name", bean.group_title);
                    intent.putExtra("status", "2");
                    context.startActivity(intent);
                }
            });
        }else  if(status.equals("3")){
            holder.ai_weixin_status.setText("审核失败");
            holder.ai_weixin_status.setTextColor(context.getResources().getColor(R.color.white));
            holder.ai_weixin_status.setBackground(context.getResources().getDrawable(R.drawable.set_weixinqunred_yuanjiao));
            holder.ai_weixinqun_genghuan.setText("重新申请");
            holder.ai_weixinqun_genghuan.setTextColor(context.getResources().getColor(R.color.red));
            holder.ai_weixinqun_genghuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, AiEstablishActivity.class);
                    intent.putExtra("tmpid", bean.tmp_id+"");
                    intent.putExtra("wx_service_user",bean.wx_service_user);
                    intent.putExtra("group_title",bean.group_title);
                    intent.putExtra("action","reRegWxGroup");
                    context.startActivity(intent);
                }
            });
        }else if(status.equals("4")){
            holder.ai_weixin_status.setText("违规退群");
            holder.ai_weixin_status.setTextColor(context.getResources().getColor(R.color.white));
            holder.ai_weixin_status.setBackground(context.getResources().getDrawable(R.drawable.set_weixinqunred_yuanjiao));

            holder.ai_weixinqun_genghuan.setText("重新申请");
            holder.ai_weixinqun_genghuan.setTextColor(context.getResources().getColor(R.color.red));
            holder.ai_weixinqun_genghuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, AiEstablishActivity.class);
                    intent.putExtra("tmpid", bean.tmp_id+"");
                    intent.putExtra("wx_service_user",bean.wx_service_user);
                    intent.putExtra("group_title",bean.group_title);
                    intent.putExtra("action","reRegWxGroup");
                    context.startActivity(intent);
                }
            });
        }else if(status.equals("9")){
            holder.ai_weixin_status.setText("群推正常");
            holder.ai_weixinqun_genghuan.setText("更换助理");
            holder.ai_weixinqun_genghuan.setTextColor(context.getResources().getColor(R.color.red));
            holder.ai_weixinqun_genghuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, AiOpeningaiActivity.class);
                    intent.putExtra("tmpid", bean.tmp_id);
                    intent.putExtra("name", bean.group_title);
                    intent.putExtra("wx_service_user", "");
                    intent.putExtra("status", "9");
                    context.startActivity(intent);
                }
            });
        }
        if(holder.tv_ai_sethuanying.getText().toString().equals("所有群")){
            holder.line.setVisibility(View.GONE);
        }else{
            holder.tv_ai_sethuanying.setText(bean.group_title);
        }
             holder.ai_weixin_bianhao.setText("群编号:"+bean.tmp_id);

    }

    /*
     *得到总条数，返回data.size() 就可以了
     */
    @Override
    public int getItemCount() {
        int i;
        if(data.size()>=10){
            i=10;
        }else{
            i=data.size();
        }
        return i;
    }

    /*
    也是getview
     */
    class MyViewHolder extends RecyclerView.ViewHolder{


        private TextView tv_ai_sethuanying;
        private TextView ai_weixinqun_genghuan;
        private TextView ai_weixin_bianhao;
        private TextView ai_weixin_status;
        private LinearLayout line;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_ai_sethuanying = itemView.findViewById(R.id.tv_ai_sethuanying);
            ai_weixinqun_genghuan = itemView.findViewById(R.id.ai_weixinqun_genghuan);
            ai_weixin_bianhao = itemView.findViewById(R.id.ai_weixin_bianhao);
            ai_weixin_status= itemView.findViewById(R.id.ai_weixin_status);
            line = itemView.findViewById(R.id.line);

        }
    }


}
