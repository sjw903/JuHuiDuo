package com.android.jdhshop.malladapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.jdhshop.R;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.mallbean.MallGoodsCarListBean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * 购物车  适配器
 * */
public class MallGoodsCartAdapter extends BaseAdapter {

    private Context context;
    private List<MallGoodsCarListBean> lists;
    private SubClickListener subClickListener;

    public MallGoodsCartAdapter(Context context, List<MallGoodsCarListBean> list) {
        this.context = context;
        this.lists = list;
    }
    public void setsubClickListener(SubClickListener topicClickListener) {
        this.subClickListener = topicClickListener;
    }

    public interface SubClickListener {
        void OntopicClickListener(View v, String detail, boolean bool, int posit);
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
            convertView = View.inflate(context, R.layout.item_shoppingcart, null);
            viewHold = new ViewHold();
            viewHold.tv_del = convertView.findViewById(R.id.itemshopcart_del);
            viewHold.tv_name = convertView.findViewById( R.id.itemshopcart_name );
            viewHold.tv_type = convertView.findViewById( R.id.itemshopcart_type );
            viewHold.tv_jifen = convertView.findViewById( R.id.itemshopcart_jifen );
            viewHold.tv_num = convertView.findViewById( R.id.itemshopcart_num );
            viewHold.iv_img = convertView.findViewById( R.id.itemshopcart_img );
            viewHold.iv_ischeck = convertView.findViewById( R.id.itemshopcart_check );
            viewHold.iv_jia = convertView.findViewById( R.id.itemshopcart_jia );
            viewHold.iv_jian = convertView.findViewById( R.id.itemshopcart_jian );
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.tv_name.setText( lists.get( position ).goods_name );
//        viewHold.tv_type.setText( lists.get( position ). );
        viewHold.tv_num.setText( lists.get( position ).goods_num );
        Glide.with( context ).load(lists.get( position ).img.contains("http")?lists.get( position ).img: Constants.APP_IP +lists.get( position ).img ).into( viewHold.iv_img );
        viewHold.tv_jifen.setText( lists.get( position ).price+"元" );
        try {
            JSONArray jsonArray = new JSONArray(lists.get( position ).sku_arr );
            String strtype="";
            for (int i = 0;i<jsonArray.length();i++){
                strtype+=jsonArray.getJSONObject(i).getString("attribute_name")+":"+jsonArray.getJSONObject(i).getString("value")+" ";
            }
            viewHold.tv_type.setText( strtype );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.get(position).ischeck){
            viewHold.iv_ischeck.setImageResource(R.drawable.sel_check);
        }else {
            viewHold.iv_ischeck.setImageResource(R.drawable.unsel_check);
        }
        viewHold.iv_ischeck.setOnClickListener( new View.OnClickListener() {//是否选中
            @Override
            public void onClick(View view) {
                if (lists.get(position).ischeck){
                    lists.get(position).ischeck = false;
                    if (subClickListener!=null){
                        subClickListener.OntopicClickListener(view,"ischeck",false,position);
                    }
                }else{
                    lists.get(position).ischeck = true;
                    if (subClickListener!=null){
                        subClickListener.OntopicClickListener(view,"ischeck",true,position);
                    }
                }
                notifyDataSetChanged();
            }
        } );
        final MallGoodsCarListBean shops = lists.get(position);
        viewHold.tv_del.setOnClickListener( new View.OnClickListener() {//删除
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener(view,"del",false,position);
                    slideLayout.closeMenu();
                }
            }
        } );
        final ViewHold finalViewHold = viewHold;
        viewHold.iv_jia.setOnClickListener( new View.OnClickListener() {//加数量
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener(view,"jia",false,position);
                }
                notifyDataSetChanged();
            }
        } );

        viewHold.iv_jian.setOnClickListener( new View.OnClickListener() {//减数量
            @Override
            public void onClick(View view) {
                if (subClickListener!=null){
                    subClickListener.OntopicClickListener(view,"jian",false,position);
                }
                notifyDataSetChanged();
            }
        } );
        SlideLayout slideLayout = (SlideLayout) convertView;
        slideLayout.setOnStateChangeListener(new MyOnStateChangeListener());
        return convertView;
    }
    public SlideLayout slideLayout = null;
    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener
    {

        @Override
        public void onOpen(SlideLayout layout) {

            slideLayout = layout;
        }

        @Override
        public void onMove(SlideLayout layout) {
            if (slideLayout != null && slideLayout !=layout)
            {
                slideLayout.closeMenu();
            }
        }

        @Override
        public void onClose(SlideLayout layout) {
            if (slideLayout == layout)
            {
                slideLayout = null;
            }
        }
    }
    private static class ViewHold {
        private TextView tv_del;
        private TextView tv_name,tv_type,tv_jifen,tv_num;
        private ImageView iv_img,iv_ischeck,iv_jia,iv_jian;

    }

}
