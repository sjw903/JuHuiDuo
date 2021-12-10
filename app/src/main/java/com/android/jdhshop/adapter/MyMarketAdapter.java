package com.android.jdhshop.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.bean.TeamListBean;
import com.android.jdhshop.config.Constants;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Pattern;

public class MyMarketAdapter extends CommonAdapter<TeamListBean.Teamlist> {

    public MyMarketAdapter(Context context, int layoutId, List<TeamListBean.Teamlist> datas) {
        super(context, layoutId, datas);
    }
    private static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }
    /**
     * Base64解密字符串
     * @param content -- 待解密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    private String base64Decode(String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "utf-8";
        }
        byte[] contentByte = Base64.decode(content, Base64.DEFAULT);
        try {
            return new String(contentByte, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void convert(ViewHolder viewHolder, TeamListBean.Teamlist item, int position) {
        ImageView imageView = viewHolder.getView(R.id.image_icon);
        Glide.with(mContext).load(item.getAvatar()==null?"":item.getAvatar()).dontAnimate().error(R.mipmap.icon_defult_boy).skipMemoryCache(true).into(imageView);
        if(item.remark==null||"".equals(item.remark)){
            if(item.getName()!=null && isBase64(item.getName())){
                viewHolder.setText( R.id.title_tv,base64Decode(item.getName(),"utf-8"));
            }else{
                viewHolder.setText( R.id.title_tv,item.getName()+"  ");
            }
        }else{
            viewHolder.setText( R.id.title_tv,item.remark+" ");

        }
        //动态设置drawableLeft属性
        if("Y".equals(item.getIs_buy())){
            Drawable drawable1 =mContext.getResources().getDrawable(R.mipmap.gouwu);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            ((TextView)viewHolder.getView(R.id.title_tv)).setCompoundDrawables(null, null, drawable1, null);
        }else{
            ((TextView)viewHolder.getView(R.id.title_tv)).setCompoundDrawables(null, null, null, null);
        }
        viewHolder.setText(R.id.jishu_tv,item.getGroup_name());
        viewHolder.setText(R.id.yaoqingren_tv,"他的邀请："+item.getReferrer_num());

        if(item.getRegister_time()==null||item.getRegister_time().equals("")){
            if(isBase64(item.getReferrer_name())){
                viewHolder.setText( R.id.title_tv,base64Decode(item.getReferrer_name(),"utf-8"));
            }else{
                viewHolder.setText(R.id.yaoqingren_ont_tv,"邀请人："+item.getReferrer_name());
            }
        }else{
            viewHolder.setText(R.id.yaoqingren_ont_tv,"时间："+item.getRegister_time().split(" ")[0]);
        }
    }
}
