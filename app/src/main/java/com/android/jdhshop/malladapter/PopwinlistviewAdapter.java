package com.android.jdhshop.malladapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.jdhshop.R;
import com.android.jdhshop.mallbean.Selectbean;
import com.android.jdhshop.mallbean.Skuxianshibean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Set;

/**
 * popwindow里listView的布局
 *
 * @author Administrator
 */
public class PopwinlistviewAdapter extends BaseAdapter {
    private Context context;
    private List<Skuxianshibean> list;
    private TagAdapter<String> madapter;
    private SubClickListener subClickListener;
    private boolean bale;
    private List<Selectbean> selectbeans;
    public PopwinlistviewAdapter(Context context, List<Skuxianshibean> foodDatas) {
        this.context = context;
        this.list = foodDatas;

    }

    public void setSelectbeans(List<Selectbean> selectbeans) {
        this.selectbeans = selectbeans;
    }

    public void setsubClickListener(SubClickListener topicClickListener) {
        this.subClickListener = topicClickListener;
    }

    public interface SubClickListener {
        void OntopicClickListeners(View v, boolean detail, int posit, String str);
    }

    @Override
    public int getCount() {

        return list.size();

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int positiona, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.popwindow_listview_item, null);
            viewHold = new ViewHold();
            viewHold.gridView = convertView.findViewById(R.id.tagflowlayout);
            viewHold.tv_name = convertView.findViewById(R.id.popwindow_name);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.tv_name.setText(list.get(positiona).attribute_name);

        String sss = list.get(positiona).value_list.replace("[", "");
        sss = sss.replace("]", "");
        sss = sss.replace("\"", "");
        final String[] str = sss.split(",");
        viewHold.gridView.setAdapter(madapter = new TagAdapter<String>(str) {
            @Override
            public View getView(FlowLayout parents, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv, parents, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public boolean setSelected(int position, String s) {
                if(selectbeans!=null){
                   return s.equals(selectbeans.get(positiona).getValue());
                }
                return s.equals("000000");
            }
        });
        viewHold.gridView.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (subClickListener != null) {
                    subClickListener.OntopicClickListeners(view, bale, positiona, str[position]);
                }
                return false;
            }
        });

        viewHold.gridView.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (!selectPosSet.toString().equals("[]")) {
                    bale = true;
                } else {
                    bale = false;
                }
            }

        });
        return convertView;
    }

    private static class ViewHold {
        private TagFlowLayout gridView;
        private TextView tv_name;

    }

}
