package com.android.jdhshop.juduohui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.alibaba.fastjson.JSONArray;
import com.android.jdhshop.R;
public class NewsTypeSetAdapter extends RecyclerView.Adapter<NewsTypeSetAdapter.NewsTypeViewHolder> {
    JSONArray types;
    ItemCheckChangeListen listener;

    public NewsTypeSetAdapter(JSONArray type_array,ItemCheckChangeListen itemCheckChangeListen){
        types = type_array;
        listener = itemCheckChangeListen;
    }

    @Override
    public NewsTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsTypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_type_set_dialog_item,parent,false));
    }

    @Override
    public void onBindViewHolder(NewsTypeViewHolder h, int i) {
        Log.d("TAG", "onBindViewHolder: " + types.getJSONObject(i).getString("type_name") + "= " +types.getJSONObject(i).getBooleanValue("state"));
        h.type_switch.setText(types.getJSONObject(i).getString("type_name"));
        h.type_switch.setChecked(types.getJSONObject(i).getBooleanValue("state"));
        final int j = types.getJSONObject(i).getIntValue("id");
        h.type_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.change(buttonView,j,isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public interface ItemCheckChangeListen{
        void change(CompoundButton v, int i,boolean isChecked);
    }

    public static class NewsTypeViewHolder extends RecyclerView.ViewHolder {
        Switch type_switch;
        public NewsTypeViewHolder(View v) {
            super(v);
            type_switch = v.findViewById(R.id.news_type_item_switch);
        }
    }
}