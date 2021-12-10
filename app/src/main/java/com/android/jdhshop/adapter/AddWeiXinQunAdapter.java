package com.android.jdhshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.activity.AiEstablishActivity;
import com.android.jdhshop.activity.AiOpeningaiActivity;
import com.android.jdhshop.activity.SetAssistantActivity;
import com.android.jdhshop.bean.WeiXinQunXinXIBean;
import com.android.jdhshop.common.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的群信息页面适配器
 */
public class AddWeiXinQunAdapter extends RecyclerView.Adapter<AddWeiXinQunAdapter.MyViewHolder>{
    private final Context context;
    List<WeiXinQunXinXIBean> data = new ArrayList<WeiXinQunXinXIBean>();
    private List<String> addid=new ArrayList<>();
    /*
    自定义的构造方法 ，传入上下文对象context 和data数据
     */
    public AddWeiXinQunAdapter(Context context, List<WeiXinQunXinXIBean> data){
        this.context = context;
        this.data = data;
    }
    /*
    getview
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.addweixinqun_item_recy,null);
        return new MyViewHolder(view);
    }
    /*
    绑定数据
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WeiXinQunXinXIBean bean = data.get(position);
        String status = bean.status;
        String huoquaddid = SPUtils.getStringData(context, "huoquaddid", "");
        if(huoquaddid.indexOf(bean.tmp_id) !=-1){
            holder.add_relat.setBackground(context.getResources().getDrawable(R.drawable.set_weixinqunadd_yuanjiao));
            bean.ischeck="true";
            addid.add(bean.tmp_id);
        }

            holder.tv_ai_sethuanying.setText(bean.group_title);


            holder.add_relat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bean.ischeck.equals("false")){
                        holder.add_relat.setBackground(context.getResources().getDrawable(R.drawable.set_weixinqunadd_yuanjiao));
                        bean.ischeck="true";
                        addid.add(bean.tmp_id);
                    }else if(bean.ischeck.equals("true")){
                        holder.add_relat.setBackground(context.getResources().getDrawable(R.drawable.set_weixinqunadd1_yuanjiao));
                        bean.ischeck="false";
                        addid.remove(bean.tmp_id);
                    }
                    SPUtils.saveStringData(context, "addid", addid+"");

                    Log.d("aaaaaaaaaaaaa", addid+"");
                }
            });

    }

    /*
     *得到总条数，返回data.size() 就可以了
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /*
    也是getview
     */
    class MyViewHolder extends RecyclerView.ViewHolder{


        private TextView tv_ai_sethuanying;
        private RelativeLayout add_relat;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_ai_sethuanying = itemView.findViewById(R.id.tv_ai_sethuanying);
            add_relat = itemView.findViewById(R.id.add_relat);

        }
    }


}
