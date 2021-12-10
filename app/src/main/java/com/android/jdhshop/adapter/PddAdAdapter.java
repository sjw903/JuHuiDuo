package com.android.jdhshop.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.android.jdhshop.base.BaseLogDZiYuan;
import com.android.jdhshop.config.Constants;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.android.jdhshop.R;

import java.util.List;

public class PddAdAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    List<String> names;
    public PddAdAdapter(int layoutResId, @Nullable List<String> data,List<String> names) {
        super(layoutResId, data);
        this.names=names;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.txt_name,names.get(helper.getAdapterPosition()));
        ImageView imageView=helper.getView(R.id.img);
        switch ((helper.getAdapterPosition()+1)%7){
            case 0:
                BaseLogDZiYuan.LogDingZiYuan(imageView, "events_card_a.png");
                break;
            case 1:
                BaseLogDZiYuan.LogDingZiYuan(imageView, "events_card_b.png");
                break;
            case 2:
                BaseLogDZiYuan.LogDingZiYuan(imageView, "events_card_c.png");
                break;
            case 3:
                BaseLogDZiYuan.LogDingZiYuan(imageView, "events_card_d.png");
                break;
            case 4:
                BaseLogDZiYuan.LogDingZiYuan(imageView, "events_card_e.png");
                break;
                case 5:
                    BaseLogDZiYuan.LogDingZiYuan(imageView, "events_card_f.png");
                break;
            case 6:
                BaseLogDZiYuan.LogDingZiYuan(imageView, "events_card_g.png");
                break;


        }
    }
}
