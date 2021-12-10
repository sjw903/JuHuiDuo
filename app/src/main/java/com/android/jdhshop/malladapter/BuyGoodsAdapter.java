package com.android.jdhshop.malladapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.mallbean.BuyCarBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class BuyGoodsAdapter extends CommonAdapter<BuyCarBean> {
    public BuyGoodsAdapter(Context context, int layoutId, List<BuyCarBean> datas) {
        super(context, layoutId, datas);
    }

    public Onchange getOnchange() {
        return onchange;
    }

    public void setOnchange(Onchange onchange) {
        this.onchange = onchange;
    }

    private Onchange onchange;

    @Override
    protected void convert(ViewHolder helper, final BuyCarBean buyCarBean, final int position) {
        ImageView imageView=helper.getView(R.id.img_shop);
        Glide.with(mContext).load(buyCarBean.getImg().contains("http")?buyCarBean.getImg(): Constants.APP_IP+buyCarBean.getImg()).error(R.drawable.no_banner).dontAnimate().into(imageView);
        helper.setText(R.id.txt_name,buyCarBean.getGoods_name());
        helper.setText(R.id.txt_num,""+buyCarBean.getNum());
        helper.setText(R.id.txt_price,buyCarBean.getPrice());
        TextView tx2_2 = helper.getView(R.id.txt_old_price);
        tx2_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tx2_2.setText(buyCarBean.getOld_price());
        String temp = "";
        for(int i=0;i<buyCarBean.getSelectbeans().size();i++){
            if(i!=buyCarBean.getSelectbeans().size()-1){
                temp += buyCarBean.getSelectbeans().get(i).getValue() + ",";
            }else {
                temp += buyCarBean.getSelectbeans().get(i).getValue();
            }
        }
        helper.setText(R.id.txt_attribute,temp);
        helper.getView(R.id.img_jia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.get(position).setNum((Integer.valueOf(buyCarBean.getNum())+1)+"");
                notifyDataSetChanged();
                onchange.change();
            }
        });
        helper.getView(R.id.img_jian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(mDatas.get(position).getNum())==1){
                    return;
                }
                mDatas.get(position).setNum((Integer.valueOf(buyCarBean.getNum())-1)+"");
                notifyDataSetChanged();
                onchange.change();
            }
        });
    }
    public interface Onchange{
        void change();
    }
}
